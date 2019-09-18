package com.chicago.ext.model;

public class EnumTypes
{
    public enum PropertyType
    {
        APARTMENT(0),
        ROOM(1),
        SHARE(2),
        HOUSE(3),
        HOUSE_PART(4),
        OFFICE(5),
        RETAIL_SPACE(6),
        WAREHOUSE(7),
        RESTAURANT(8),
        GARAGE(9),
        AUTO_SERVICE(11);

        private int _value;

        PropertyType(int value)
        {
            this._value = value;
        }

        public int getValue()
        {
            return _value;
        }
    }

    public enum Market
    {
        MARKET_NOT_IMPORTANT(0),
        FIRST(1),
        SECOND(2);


        private int _value;

        Market(int value)
        {
            this._value = value;
        }

        public int getValue()
        {
            return _value;
        }
    }

    public enum ViewFromWindow
    {
        VIEW_NOT_IMPORTANT(0),
        STREET_VIEW(1),
        BACKYARD_VIEW(2);

        private int _value;

        ViewFromWindow(int value)
        {
            this._value = value;
        }

        public int getValue()
        {
            return _value;
        }
    }

    public enum CeilingHeight
    {
        HEIGHT_NOT_IMPORTANT(0),
        FROM_2_5(1),
        FROM_3_0(2),
        FROM_3_5(3),
        FROM_4_0(4);

        private int _value;

        CeilingHeight(int value)
        {
            this._value = value;
        }

        public int getValue()
        {
            return _value;
        }
    }

    public enum FloorBeamsMaterial
    {
        WOOD_WOOD(0),
        METAL_OOD(1),
        METAL_CONCRETE(2);

        private int _value;

        FloorBeamsMaterial(int value)
        {
            this._value = value;
        }

        public int getValue()
        {
            return _value;
        }
    }

}
