package com.chicago.ext.components;

import com.chicago.common.components.kafka.KafkaMessageProducer;
import com.chicago.common.core.AbstractComponent;
import com.chicago.common.core.AbstractEventDispatcher;
import com.chicago.common.core.ComponentManager;
import com.chicago.common.core.ConfigAccessor;
import com.chicago.common.core.EventBase;
import com.chicago.common.core.EventHandler;
import com.chicago.dto.Common;
import com.chicago.dto.Searchfilters;
import com.chicago.dto.Searchfiltersmessages;
import com.chicago.ext.bll.SearchFiltersBll;
import com.chicago.ext.model.SearchFiltersModel;
import com.chicago.ext.util.ResponseFactoryUtil;
import com.google.protobuf.Message;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SearchFiltersRequests extends AbstractComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(SearchFiltersRequests.class);
    // Can not be injected, class created with new(). Will use ServiceLocator
    private SearchFiltersBll _searchFiltersBll;

    public SearchFiltersRequests(ComponentManager cm) throws ClassNotFoundException
    {
        _ed = cm.getResource(AbstractEventDispatcher.class.getName());
        _ed.registerHandler(Searchfiltersmessages.SearchFiltersRequest.class, new SearchFiltersEventHandler());
        _ed.registerHandler(Searchfiltersmessages.SearchFiltersCatalogRequest.class, new SearchFiltersCatalogEventHandler());
        // Response
        KafkaMessageProducer producer = cm.getResource(KafkaMessageProducer.class.getName());
        _ed.registerHandler(Searchfiltersmessages.SearchFiltersResponse.class, producer.new MessageEventHandler());
        _ed.registerHandler(Searchfiltersmessages.SearchFiltersCatalogResponse.class, producer.new MessageEventHandler());
    }

    public boolean init(ConfigAccessor ca)
    {
        _searchFiltersBll = ServiceLocatorFactory.getInstance().find("servicelocator").getService(SearchFiltersBll.class);
        return true;
    }

    public static void registerComponentFactories()
    {
        ComponentManager.registerComponentFactory(new Exception().getStackTrace()[0].getClassName());
    }

    class SearchFiltersEventHandler implements EventHandler<Searchfiltersmessages.SearchFiltersRequest>
    {
        @Override
        public void handleEvent(Searchfiltersmessages.SearchFiltersRequest event, String transactionId)
        {
            Message response;
            try
            {
                SearchFiltersModel.SearchFilters model = new SearchFiltersModel.SearchFiltersConvertor().fromDto(event.getSearchfilters());
                _searchFiltersBll.getCianSearchFilters(model);
                List<Searchfilters.Property> propertyList = new ArrayList<>();
                Searchfilters.Property p = Searchfilters.Property.newBuilder().setPropertyId("1223").build();
                propertyList.add(p);
                response = Searchfiltersmessages.SearchFiltersResponse
                        .newBuilder()
                        .addAllProperty(propertyList)
                        .build();
            } catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex, Common.VoidResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }

    class SearchFiltersCatalogEventHandler implements EventHandler<Searchfiltersmessages.SearchFiltersCatalogRequest>
    {
        @Override
        public void handleEvent(Searchfiltersmessages.SearchFiltersCatalogRequest event, String transactionId)
        {
            Message response;
            try
            {
                List<SearchFiltersModel.SearchFilters> modelFilters = _searchFiltersBll.getSearchFilters(event.getUserId());
                List<Searchfilters.SearchFilters> dtoFilters = new ArrayList<>();
                SearchFiltersModel.SearchFiltersConvertor convertor = new SearchFiltersModel.SearchFiltersConvertor();

                for(SearchFiltersModel.SearchFilters filter : modelFilters)
                {
                    dtoFilters.add(convertor.toDto(filter));
                }
                response = Searchfiltersmessages.SearchFiltersCatalogResponse
                        .newBuilder()
                        .addAllSearchfilters(dtoFilters)
                        .build();
            } catch (Exception ex)
            {
                response = ResponseFactoryUtil.createErrorResponse(ex, Common.VoidResponse.class);
            }
            _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), response, transactionId));
            LOG.info("Published real-time response on request with transaction id: {}", transactionId);
        }
    }
}
