/**
 * @fileoverview
 * @enhanceable
 * @suppress {messageConventions} JS Compiler reports an error if a variable or
 *     field starts with 'MSG_' and isn't a translatable message.
 * @public
 */
// GENERATED CODE -- DO NOT EDIT!

var jspb = require('google-protobuf');
var goog = jspb;
var global = Function('return this')();

goog.exportSymbol('proto.com.chicago.dto.ApplicationConfig', null, global);
goog.exportSymbol('proto.com.chicago.dto.CassandraConfig', null, global);
goog.exportSymbol('proto.com.chicago.dto.Component', null, global);
goog.exportSymbol('proto.com.chicago.dto.Component.ComponentType', null, global);
goog.exportSymbol('proto.com.chicago.dto.ComponentsConfig', null, global);
goog.exportSymbol('proto.com.chicago.dto.KafkaConfig', null, global);
goog.exportSymbol('proto.com.chicago.dto.ShutdownConfig', null, global);
goog.exportSymbol('proto.com.chicago.dto.ZooKeeperConfig', null, global);

/**
 * Generated by JsPbCodeGenerator.
 * @param {Array=} opt_data Optional initial data array, typically from a
 * server response, or constructed directly in Javascript. The array is used
 * in place and becomes part of the constructed object. It is not cloned.
 * If no data is provided, the constructed object will be empty, but still
 * valid.
 * @extends {jspb.Message}
 * @constructor
 */
proto.com.chicago.dto.ApplicationConfig = function(opt_data) {
  jspb.Message.initialize(this, opt_data, 0, -1, null, null);
};
goog.inherits(proto.com.chicago.dto.ApplicationConfig, jspb.Message);
if (goog.DEBUG && !COMPILED) {
  proto.com.chicago.dto.ApplicationConfig.displayName = 'proto.com.chicago.dto.ApplicationConfig';
}


if (jspb.Message.GENERATE_TO_OBJECT) {
/**
 * Creates an object representation of this proto suitable for use in Soy templates.
 * Field names that are reserved in JavaScript and will be renamed to pb_name.
 * To access a reserved field use, foo.pb_<name>, eg, foo.pb_default.
 * For the list of reserved names please see:
 *     com.google.apps.jspb.JsClassTemplate.JS_RESERVED_WORDS.
 * @param {boolean=} opt_includeInstance Whether to include the JSPB instance
 *     for transitional soy proto support: http://goto/soy-param-migration
 * @return {!Object}
 */
proto.com.chicago.dto.ApplicationConfig.prototype.toObject = function(opt_includeInstance) {
  return proto.com.chicago.dto.ApplicationConfig.toObject(opt_includeInstance, this);
};


/**
 * Static version of the {@see toObject} method.
 * @param {boolean|undefined} includeInstance Whether to include the JSPB
 *     instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @param {!proto.com.chicago.dto.ApplicationConfig} msg The msg instance to transform.
 * @return {!Object}
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.com.chicago.dto.ApplicationConfig.toObject = function(includeInstance, msg) {
  var f, obj = {
    applicationName: jspb.Message.getFieldWithDefault(msg, 1, ""),
    instanceId: jspb.Message.getFieldWithDefault(msg, 2, "")
  };

  if (includeInstance) {
    obj.$jspbMessageInstance = msg;
  }
  return obj;
};
}


/**
 * Deserializes binary data (in protobuf wire format).
 * @param {jspb.ByteSource} bytes The bytes to deserialize.
 * @return {!proto.com.chicago.dto.ApplicationConfig}
 */
proto.com.chicago.dto.ApplicationConfig.deserializeBinary = function(bytes) {
  var reader = new jspb.BinaryReader(bytes);
  var msg = new proto.com.chicago.dto.ApplicationConfig;
  return proto.com.chicago.dto.ApplicationConfig.deserializeBinaryFromReader(msg, reader);
};


/**
 * Deserializes binary data (in protobuf wire format) from the
 * given reader into the given message object.
 * @param {!proto.com.chicago.dto.ApplicationConfig} msg The message object to deserialize into.
 * @param {!jspb.BinaryReader} reader The BinaryReader to use.
 * @return {!proto.com.chicago.dto.ApplicationConfig}
 */
proto.com.chicago.dto.ApplicationConfig.deserializeBinaryFromReader = function(msg, reader) {
  while (reader.nextField()) {
    if (reader.isEndGroup()) {
      break;
    }
    var field = reader.getFieldNumber();
    switch (field) {
    case 1:
      var value = /** @type {string} */ (reader.readString());
      msg.setApplicationName(value);
      break;
    case 2:
      var value = /** @type {string} */ (reader.readString());
      msg.setInstanceId(value);
      break;
    default:
      reader.skipField();
      break;
    }
  }
  return msg;
};


/**
 * Serializes the message to binary data (in protobuf wire format).
 * @return {!Uint8Array}
 */
proto.com.chicago.dto.ApplicationConfig.prototype.serializeBinary = function() {
  var writer = new jspb.BinaryWriter();
  proto.com.chicago.dto.ApplicationConfig.serializeBinaryToWriter(this, writer);
  return writer.getResultBuffer();
};


/**
 * Serializes the given message to binary data (in protobuf wire
 * format), writing to the given BinaryWriter.
 * @param {!proto.com.chicago.dto.ApplicationConfig} message
 * @param {!jspb.BinaryWriter} writer
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.com.chicago.dto.ApplicationConfig.serializeBinaryToWriter = function(message, writer) {
  var f = undefined;
  f = message.getApplicationName();
  if (f.length > 0) {
    writer.writeString(
      1,
      f
    );
  }
  f = message.getInstanceId();
  if (f.length > 0) {
    writer.writeString(
      2,
      f
    );
  }
};


/**
 * optional string application_name = 1;
 * @return {string}
 */
proto.com.chicago.dto.ApplicationConfig.prototype.getApplicationName = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 1, ""));
};


/** @param {string} value */
proto.com.chicago.dto.ApplicationConfig.prototype.setApplicationName = function(value) {
  jspb.Message.setProto3StringField(this, 1, value);
};


/**
 * optional string instance_id = 2;
 * @return {string}
 */
proto.com.chicago.dto.ApplicationConfig.prototype.getInstanceId = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 2, ""));
};


/** @param {string} value */
proto.com.chicago.dto.ApplicationConfig.prototype.setInstanceId = function(value) {
  jspb.Message.setProto3StringField(this, 2, value);
};



/**
 * Generated by JsPbCodeGenerator.
 * @param {Array=} opt_data Optional initial data array, typically from a
 * server response, or constructed directly in Javascript. The array is used
 * in place and becomes part of the constructed object. It is not cloned.
 * If no data is provided, the constructed object will be empty, but still
 * valid.
 * @extends {jspb.Message}
 * @constructor
 */
proto.com.chicago.dto.KafkaConfig = function(opt_data) {
  jspb.Message.initialize(this, opt_data, 0, -1, proto.com.chicago.dto.KafkaConfig.repeatedFields_, null);
};
goog.inherits(proto.com.chicago.dto.KafkaConfig, jspb.Message);
if (goog.DEBUG && !COMPILED) {
  proto.com.chicago.dto.KafkaConfig.displayName = 'proto.com.chicago.dto.KafkaConfig';
}
/**
 * List of repeated fields within this message type.
 * @private {!Array<number>}
 * @const
 */
proto.com.chicago.dto.KafkaConfig.repeatedFields_ = [2];



if (jspb.Message.GENERATE_TO_OBJECT) {
/**
 * Creates an object representation of this proto suitable for use in Soy templates.
 * Field names that are reserved in JavaScript and will be renamed to pb_name.
 * To access a reserved field use, foo.pb_<name>, eg, foo.pb_default.
 * For the list of reserved names please see:
 *     com.google.apps.jspb.JsClassTemplate.JS_RESERVED_WORDS.
 * @param {boolean=} opt_includeInstance Whether to include the JSPB instance
 *     for transitional soy proto support: http://goto/soy-param-migration
 * @return {!Object}
 */
proto.com.chicago.dto.KafkaConfig.prototype.toObject = function(opt_includeInstance) {
  return proto.com.chicago.dto.KafkaConfig.toObject(opt_includeInstance, this);
};


/**
 * Static version of the {@see toObject} method.
 * @param {boolean|undefined} includeInstance Whether to include the JSPB
 *     instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @param {!proto.com.chicago.dto.KafkaConfig} msg The msg instance to transform.
 * @return {!Object}
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.com.chicago.dto.KafkaConfig.toObject = function(includeInstance, msg) {
  var f, obj = {
    servers: jspb.Message.getFieldWithDefault(msg, 1, ""),
    consumerTopicList: jspb.Message.getRepeatedField(msg, 2),
    consumerGroup: jspb.Message.getFieldWithDefault(msg, 3, ""),
    producerTopic: jspb.Message.getFieldWithDefault(msg, 4, "")
  };

  if (includeInstance) {
    obj.$jspbMessageInstance = msg;
  }
  return obj;
};
}


/**
 * Deserializes binary data (in protobuf wire format).
 * @param {jspb.ByteSource} bytes The bytes to deserialize.
 * @return {!proto.com.chicago.dto.KafkaConfig}
 */
proto.com.chicago.dto.KafkaConfig.deserializeBinary = function(bytes) {
  var reader = new jspb.BinaryReader(bytes);
  var msg = new proto.com.chicago.dto.KafkaConfig;
  return proto.com.chicago.dto.KafkaConfig.deserializeBinaryFromReader(msg, reader);
};


/**
 * Deserializes binary data (in protobuf wire format) from the
 * given reader into the given message object.
 * @param {!proto.com.chicago.dto.KafkaConfig} msg The message object to deserialize into.
 * @param {!jspb.BinaryReader} reader The BinaryReader to use.
 * @return {!proto.com.chicago.dto.KafkaConfig}
 */
proto.com.chicago.dto.KafkaConfig.deserializeBinaryFromReader = function(msg, reader) {
  while (reader.nextField()) {
    if (reader.isEndGroup()) {
      break;
    }
    var field = reader.getFieldNumber();
    switch (field) {
    case 1:
      var value = /** @type {string} */ (reader.readString());
      msg.setServers(value);
      break;
    case 2:
      var value = /** @type {string} */ (reader.readString());
      msg.addConsumerTopic(value);
      break;
    case 3:
      var value = /** @type {string} */ (reader.readString());
      msg.setConsumerGroup(value);
      break;
    case 4:
      var value = /** @type {string} */ (reader.readString());
      msg.setProducerTopic(value);
      break;
    default:
      reader.skipField();
      break;
    }
  }
  return msg;
};


/**
 * Serializes the message to binary data (in protobuf wire format).
 * @return {!Uint8Array}
 */
proto.com.chicago.dto.KafkaConfig.prototype.serializeBinary = function() {
  var writer = new jspb.BinaryWriter();
  proto.com.chicago.dto.KafkaConfig.serializeBinaryToWriter(this, writer);
  return writer.getResultBuffer();
};


/**
 * Serializes the given message to binary data (in protobuf wire
 * format), writing to the given BinaryWriter.
 * @param {!proto.com.chicago.dto.KafkaConfig} message
 * @param {!jspb.BinaryWriter} writer
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.com.chicago.dto.KafkaConfig.serializeBinaryToWriter = function(message, writer) {
  var f = undefined;
  f = message.getServers();
  if (f.length > 0) {
    writer.writeString(
      1,
      f
    );
  }
  f = message.getConsumerTopicList();
  if (f.length > 0) {
    writer.writeRepeatedString(
      2,
      f
    );
  }
  f = message.getConsumerGroup();
  if (f.length > 0) {
    writer.writeString(
      3,
      f
    );
  }
  f = message.getProducerTopic();
  if (f.length > 0) {
    writer.writeString(
      4,
      f
    );
  }
};


/**
 * optional string servers = 1;
 * @return {string}
 */
proto.com.chicago.dto.KafkaConfig.prototype.getServers = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 1, ""));
};


/** @param {string} value */
proto.com.chicago.dto.KafkaConfig.prototype.setServers = function(value) {
  jspb.Message.setProto3StringField(this, 1, value);
};


/**
 * repeated string consumer_topic = 2;
 * @return {!Array.<string>}
 */
proto.com.chicago.dto.KafkaConfig.prototype.getConsumerTopicList = function() {
  return /** @type {!Array.<string>} */ (jspb.Message.getRepeatedField(this, 2));
};


/** @param {!Array.<string>} value */
proto.com.chicago.dto.KafkaConfig.prototype.setConsumerTopicList = function(value) {
  jspb.Message.setField(this, 2, value || []);
};


/**
 * @param {!string} value
 * @param {number=} opt_index
 */
proto.com.chicago.dto.KafkaConfig.prototype.addConsumerTopic = function(value, opt_index) {
  jspb.Message.addToRepeatedField(this, 2, value, opt_index);
};


proto.com.chicago.dto.KafkaConfig.prototype.clearConsumerTopicList = function() {
  this.setConsumerTopicList([]);
};


/**
 * optional string consumer_group = 3;
 * @return {string}
 */
proto.com.chicago.dto.KafkaConfig.prototype.getConsumerGroup = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 3, ""));
};


/** @param {string} value */
proto.com.chicago.dto.KafkaConfig.prototype.setConsumerGroup = function(value) {
  jspb.Message.setProto3StringField(this, 3, value);
};


/**
 * optional string producer_topic = 4;
 * @return {string}
 */
proto.com.chicago.dto.KafkaConfig.prototype.getProducerTopic = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 4, ""));
};


/** @param {string} value */
proto.com.chicago.dto.KafkaConfig.prototype.setProducerTopic = function(value) {
  jspb.Message.setProto3StringField(this, 4, value);
};



/**
 * Generated by JsPbCodeGenerator.
 * @param {Array=} opt_data Optional initial data array, typically from a
 * server response, or constructed directly in Javascript. The array is used
 * in place and becomes part of the constructed object. It is not cloned.
 * If no data is provided, the constructed object will be empty, but still
 * valid.
 * @extends {jspb.Message}
 * @constructor
 */
proto.com.chicago.dto.ZooKeeperConfig = function(opt_data) {
  jspb.Message.initialize(this, opt_data, 0, -1, null, null);
};
goog.inherits(proto.com.chicago.dto.ZooKeeperConfig, jspb.Message);
if (goog.DEBUG && !COMPILED) {
  proto.com.chicago.dto.ZooKeeperConfig.displayName = 'proto.com.chicago.dto.ZooKeeperConfig';
}


if (jspb.Message.GENERATE_TO_OBJECT) {
/**
 * Creates an object representation of this proto suitable for use in Soy templates.
 * Field names that are reserved in JavaScript and will be renamed to pb_name.
 * To access a reserved field use, foo.pb_<name>, eg, foo.pb_default.
 * For the list of reserved names please see:
 *     com.google.apps.jspb.JsClassTemplate.JS_RESERVED_WORDS.
 * @param {boolean=} opt_includeInstance Whether to include the JSPB instance
 *     for transitional soy proto support: http://goto/soy-param-migration
 * @return {!Object}
 */
proto.com.chicago.dto.ZooKeeperConfig.prototype.toObject = function(opt_includeInstance) {
  return proto.com.chicago.dto.ZooKeeperConfig.toObject(opt_includeInstance, this);
};


/**
 * Static version of the {@see toObject} method.
 * @param {boolean|undefined} includeInstance Whether to include the JSPB
 *     instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @param {!proto.com.chicago.dto.ZooKeeperConfig} msg The msg instance to transform.
 * @return {!Object}
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.com.chicago.dto.ZooKeeperConfig.toObject = function(includeInstance, msg) {
  var f, obj = {
    servers: jspb.Message.getFieldWithDefault(msg, 1, "")
  };

  if (includeInstance) {
    obj.$jspbMessageInstance = msg;
  }
  return obj;
};
}


/**
 * Deserializes binary data (in protobuf wire format).
 * @param {jspb.ByteSource} bytes The bytes to deserialize.
 * @return {!proto.com.chicago.dto.ZooKeeperConfig}
 */
proto.com.chicago.dto.ZooKeeperConfig.deserializeBinary = function(bytes) {
  var reader = new jspb.BinaryReader(bytes);
  var msg = new proto.com.chicago.dto.ZooKeeperConfig;
  return proto.com.chicago.dto.ZooKeeperConfig.deserializeBinaryFromReader(msg, reader);
};


/**
 * Deserializes binary data (in protobuf wire format) from the
 * given reader into the given message object.
 * @param {!proto.com.chicago.dto.ZooKeeperConfig} msg The message object to deserialize into.
 * @param {!jspb.BinaryReader} reader The BinaryReader to use.
 * @return {!proto.com.chicago.dto.ZooKeeperConfig}
 */
proto.com.chicago.dto.ZooKeeperConfig.deserializeBinaryFromReader = function(msg, reader) {
  while (reader.nextField()) {
    if (reader.isEndGroup()) {
      break;
    }
    var field = reader.getFieldNumber();
    switch (field) {
    case 1:
      var value = /** @type {string} */ (reader.readString());
      msg.setServers(value);
      break;
    default:
      reader.skipField();
      break;
    }
  }
  return msg;
};


/**
 * Serializes the message to binary data (in protobuf wire format).
 * @return {!Uint8Array}
 */
proto.com.chicago.dto.ZooKeeperConfig.prototype.serializeBinary = function() {
  var writer = new jspb.BinaryWriter();
  proto.com.chicago.dto.ZooKeeperConfig.serializeBinaryToWriter(this, writer);
  return writer.getResultBuffer();
};


/**
 * Serializes the given message to binary data (in protobuf wire
 * format), writing to the given BinaryWriter.
 * @param {!proto.com.chicago.dto.ZooKeeperConfig} message
 * @param {!jspb.BinaryWriter} writer
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.com.chicago.dto.ZooKeeperConfig.serializeBinaryToWriter = function(message, writer) {
  var f = undefined;
  f = message.getServers();
  if (f.length > 0) {
    writer.writeString(
      1,
      f
    );
  }
};


/**
 * optional string servers = 1;
 * @return {string}
 */
proto.com.chicago.dto.ZooKeeperConfig.prototype.getServers = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 1, ""));
};


/** @param {string} value */
proto.com.chicago.dto.ZooKeeperConfig.prototype.setServers = function(value) {
  jspb.Message.setProto3StringField(this, 1, value);
};



/**
 * Generated by JsPbCodeGenerator.
 * @param {Array=} opt_data Optional initial data array, typically from a
 * server response, or constructed directly in Javascript. The array is used
 * in place and becomes part of the constructed object. It is not cloned.
 * If no data is provided, the constructed object will be empty, but still
 * valid.
 * @extends {jspb.Message}
 * @constructor
 */
proto.com.chicago.dto.ShutdownConfig = function(opt_data) {
  jspb.Message.initialize(this, opt_data, 0, -1, null, null);
};
goog.inherits(proto.com.chicago.dto.ShutdownConfig, jspb.Message);
if (goog.DEBUG && !COMPILED) {
  proto.com.chicago.dto.ShutdownConfig.displayName = 'proto.com.chicago.dto.ShutdownConfig';
}


if (jspb.Message.GENERATE_TO_OBJECT) {
/**
 * Creates an object representation of this proto suitable for use in Soy templates.
 * Field names that are reserved in JavaScript and will be renamed to pb_name.
 * To access a reserved field use, foo.pb_<name>, eg, foo.pb_default.
 * For the list of reserved names please see:
 *     com.google.apps.jspb.JsClassTemplate.JS_RESERVED_WORDS.
 * @param {boolean=} opt_includeInstance Whether to include the JSPB instance
 *     for transitional soy proto support: http://goto/soy-param-migration
 * @return {!Object}
 */
proto.com.chicago.dto.ShutdownConfig.prototype.toObject = function(opt_includeInstance) {
  return proto.com.chicago.dto.ShutdownConfig.toObject(opt_includeInstance, this);
};


/**
 * Static version of the {@see toObject} method.
 * @param {boolean|undefined} includeInstance Whether to include the JSPB
 *     instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @param {!proto.com.chicago.dto.ShutdownConfig} msg The msg instance to transform.
 * @return {!Object}
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.com.chicago.dto.ShutdownConfig.toObject = function(includeInstance, msg) {
  var f, obj = {
    shutdownTime: jspb.Message.getFieldWithDefault(msg, 1, 0),
    shutdownMinutesAfterEod: jspb.Message.getFieldWithDefault(msg, 2, 0),
    registerGrpcService: jspb.Message.getFieldWithDefault(msg, 3, false)
  };

  if (includeInstance) {
    obj.$jspbMessageInstance = msg;
  }
  return obj;
};
}


/**
 * Deserializes binary data (in protobuf wire format).
 * @param {jspb.ByteSource} bytes The bytes to deserialize.
 * @return {!proto.com.chicago.dto.ShutdownConfig}
 */
proto.com.chicago.dto.ShutdownConfig.deserializeBinary = function(bytes) {
  var reader = new jspb.BinaryReader(bytes);
  var msg = new proto.com.chicago.dto.ShutdownConfig;
  return proto.com.chicago.dto.ShutdownConfig.deserializeBinaryFromReader(msg, reader);
};


/**
 * Deserializes binary data (in protobuf wire format) from the
 * given reader into the given message object.
 * @param {!proto.com.chicago.dto.ShutdownConfig} msg The message object to deserialize into.
 * @param {!jspb.BinaryReader} reader The BinaryReader to use.
 * @return {!proto.com.chicago.dto.ShutdownConfig}
 */
proto.com.chicago.dto.ShutdownConfig.deserializeBinaryFromReader = function(msg, reader) {
  while (reader.nextField()) {
    if (reader.isEndGroup()) {
      break;
    }
    var field = reader.getFieldNumber();
    switch (field) {
    case 1:
      var value = /** @type {number} */ (reader.readInt32());
      msg.setShutdownTime(value);
      break;
    case 2:
      var value = /** @type {number} */ (reader.readInt32());
      msg.setShutdownMinutesAfterEod(value);
      break;
    case 3:
      var value = /** @type {boolean} */ (reader.readBool());
      msg.setRegisterGrpcService(value);
      break;
    default:
      reader.skipField();
      break;
    }
  }
  return msg;
};


/**
 * Serializes the message to binary data (in protobuf wire format).
 * @return {!Uint8Array}
 */
proto.com.chicago.dto.ShutdownConfig.prototype.serializeBinary = function() {
  var writer = new jspb.BinaryWriter();
  proto.com.chicago.dto.ShutdownConfig.serializeBinaryToWriter(this, writer);
  return writer.getResultBuffer();
};


/**
 * Serializes the given message to binary data (in protobuf wire
 * format), writing to the given BinaryWriter.
 * @param {!proto.com.chicago.dto.ShutdownConfig} message
 * @param {!jspb.BinaryWriter} writer
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.com.chicago.dto.ShutdownConfig.serializeBinaryToWriter = function(message, writer) {
  var f = undefined;
  f = message.getShutdownTime();
  if (f !== 0) {
    writer.writeInt32(
      1,
      f
    );
  }
  f = message.getShutdownMinutesAfterEod();
  if (f !== 0) {
    writer.writeInt32(
      2,
      f
    );
  }
  f = message.getRegisterGrpcService();
  if (f) {
    writer.writeBool(
      3,
      f
    );
  }
};


/**
 * optional int32 shutdown_time = 1;
 * @return {number}
 */
proto.com.chicago.dto.ShutdownConfig.prototype.getShutdownTime = function() {
  return /** @type {number} */ (jspb.Message.getFieldWithDefault(this, 1, 0));
};


/** @param {number} value */
proto.com.chicago.dto.ShutdownConfig.prototype.setShutdownTime = function(value) {
  jspb.Message.setProto3IntField(this, 1, value);
};


/**
 * optional int32 shutdown_minutes_after_eod = 2;
 * @return {number}
 */
proto.com.chicago.dto.ShutdownConfig.prototype.getShutdownMinutesAfterEod = function() {
  return /** @type {number} */ (jspb.Message.getFieldWithDefault(this, 2, 0));
};


/** @param {number} value */
proto.com.chicago.dto.ShutdownConfig.prototype.setShutdownMinutesAfterEod = function(value) {
  jspb.Message.setProto3IntField(this, 2, value);
};


/**
 * optional bool register_grpc_service = 3;
 * Note that Boolean fields may be set to 0/1 when serialized from a Java server.
 * You should avoid comparisons like {@code val === true/false} in those cases.
 * @return {boolean}
 */
proto.com.chicago.dto.ShutdownConfig.prototype.getRegisterGrpcService = function() {
  return /** @type {boolean} */ (jspb.Message.getFieldWithDefault(this, 3, false));
};


/** @param {boolean} value */
proto.com.chicago.dto.ShutdownConfig.prototype.setRegisterGrpcService = function(value) {
  jspb.Message.setProto3BooleanField(this, 3, value);
};



/**
 * Generated by JsPbCodeGenerator.
 * @param {Array=} opt_data Optional initial data array, typically from a
 * server response, or constructed directly in Javascript. The array is used
 * in place and becomes part of the constructed object. It is not cloned.
 * If no data is provided, the constructed object will be empty, but still
 * valid.
 * @extends {jspb.Message}
 * @constructor
 */
proto.com.chicago.dto.CassandraConfig = function(opt_data) {
  jspb.Message.initialize(this, opt_data, 0, -1, null, null);
};
goog.inherits(proto.com.chicago.dto.CassandraConfig, jspb.Message);
if (goog.DEBUG && !COMPILED) {
  proto.com.chicago.dto.CassandraConfig.displayName = 'proto.com.chicago.dto.CassandraConfig';
}


if (jspb.Message.GENERATE_TO_OBJECT) {
/**
 * Creates an object representation of this proto suitable for use in Soy templates.
 * Field names that are reserved in JavaScript and will be renamed to pb_name.
 * To access a reserved field use, foo.pb_<name>, eg, foo.pb_default.
 * For the list of reserved names please see:
 *     com.google.apps.jspb.JsClassTemplate.JS_RESERVED_WORDS.
 * @param {boolean=} opt_includeInstance Whether to include the JSPB instance
 *     for transitional soy proto support: http://goto/soy-param-migration
 * @return {!Object}
 */
proto.com.chicago.dto.CassandraConfig.prototype.toObject = function(opt_includeInstance) {
  return proto.com.chicago.dto.CassandraConfig.toObject(opt_includeInstance, this);
};


/**
 * Static version of the {@see toObject} method.
 * @param {boolean|undefined} includeInstance Whether to include the JSPB
 *     instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @param {!proto.com.chicago.dto.CassandraConfig} msg The msg instance to transform.
 * @return {!Object}
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.com.chicago.dto.CassandraConfig.toObject = function(includeInstance, msg) {
  var f, obj = {
    node: jspb.Message.getFieldWithDefault(msg, 1, "")
  };

  if (includeInstance) {
    obj.$jspbMessageInstance = msg;
  }
  return obj;
};
}


/**
 * Deserializes binary data (in protobuf wire format).
 * @param {jspb.ByteSource} bytes The bytes to deserialize.
 * @return {!proto.com.chicago.dto.CassandraConfig}
 */
proto.com.chicago.dto.CassandraConfig.deserializeBinary = function(bytes) {
  var reader = new jspb.BinaryReader(bytes);
  var msg = new proto.com.chicago.dto.CassandraConfig;
  return proto.com.chicago.dto.CassandraConfig.deserializeBinaryFromReader(msg, reader);
};


/**
 * Deserializes binary data (in protobuf wire format) from the
 * given reader into the given message object.
 * @param {!proto.com.chicago.dto.CassandraConfig} msg The message object to deserialize into.
 * @param {!jspb.BinaryReader} reader The BinaryReader to use.
 * @return {!proto.com.chicago.dto.CassandraConfig}
 */
proto.com.chicago.dto.CassandraConfig.deserializeBinaryFromReader = function(msg, reader) {
  while (reader.nextField()) {
    if (reader.isEndGroup()) {
      break;
    }
    var field = reader.getFieldNumber();
    switch (field) {
    case 1:
      var value = /** @type {string} */ (reader.readString());
      msg.setNode(value);
      break;
    default:
      reader.skipField();
      break;
    }
  }
  return msg;
};


/**
 * Serializes the message to binary data (in protobuf wire format).
 * @return {!Uint8Array}
 */
proto.com.chicago.dto.CassandraConfig.prototype.serializeBinary = function() {
  var writer = new jspb.BinaryWriter();
  proto.com.chicago.dto.CassandraConfig.serializeBinaryToWriter(this, writer);
  return writer.getResultBuffer();
};


/**
 * Serializes the given message to binary data (in protobuf wire
 * format), writing to the given BinaryWriter.
 * @param {!proto.com.chicago.dto.CassandraConfig} message
 * @param {!jspb.BinaryWriter} writer
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.com.chicago.dto.CassandraConfig.serializeBinaryToWriter = function(message, writer) {
  var f = undefined;
  f = message.getNode();
  if (f.length > 0) {
    writer.writeString(
      1,
      f
    );
  }
};


/**
 * optional string node = 1;
 * @return {string}
 */
proto.com.chicago.dto.CassandraConfig.prototype.getNode = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 1, ""));
};


/** @param {string} value */
proto.com.chicago.dto.CassandraConfig.prototype.setNode = function(value) {
  jspb.Message.setProto3StringField(this, 1, value);
};



/**
 * Generated by JsPbCodeGenerator.
 * @param {Array=} opt_data Optional initial data array, typically from a
 * server response, or constructed directly in Javascript. The array is used
 * in place and becomes part of the constructed object. It is not cloned.
 * If no data is provided, the constructed object will be empty, but still
 * valid.
 * @extends {jspb.Message}
 * @constructor
 */
proto.com.chicago.dto.Component = function(opt_data) {
  jspb.Message.initialize(this, opt_data, 0, -1, null, null);
};
goog.inherits(proto.com.chicago.dto.Component, jspb.Message);
if (goog.DEBUG && !COMPILED) {
  proto.com.chicago.dto.Component.displayName = 'proto.com.chicago.dto.Component';
}


if (jspb.Message.GENERATE_TO_OBJECT) {
/**
 * Creates an object representation of this proto suitable for use in Soy templates.
 * Field names that are reserved in JavaScript and will be renamed to pb_name.
 * To access a reserved field use, foo.pb_<name>, eg, foo.pb_default.
 * For the list of reserved names please see:
 *     com.google.apps.jspb.JsClassTemplate.JS_RESERVED_WORDS.
 * @param {boolean=} opt_includeInstance Whether to include the JSPB instance
 *     for transitional soy proto support: http://goto/soy-param-migration
 * @return {!Object}
 */
proto.com.chicago.dto.Component.prototype.toObject = function(opt_includeInstance) {
  return proto.com.chicago.dto.Component.toObject(opt_includeInstance, this);
};


/**
 * Static version of the {@see toObject} method.
 * @param {boolean|undefined} includeInstance Whether to include the JSPB
 *     instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @param {!proto.com.chicago.dto.Component} msg The msg instance to transform.
 * @return {!Object}
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.com.chicago.dto.Component.toObject = function(includeInstance, msg) {
  var f, obj = {
    className: jspb.Message.getFieldWithDefault(msg, 1, ""),
    resourceType: jspb.Message.getFieldWithDefault(msg, 2, 0)
  };

  if (includeInstance) {
    obj.$jspbMessageInstance = msg;
  }
  return obj;
};
}


/**
 * Deserializes binary data (in protobuf wire format).
 * @param {jspb.ByteSource} bytes The bytes to deserialize.
 * @return {!proto.com.chicago.dto.Component}
 */
proto.com.chicago.dto.Component.deserializeBinary = function(bytes) {
  var reader = new jspb.BinaryReader(bytes);
  var msg = new proto.com.chicago.dto.Component;
  return proto.com.chicago.dto.Component.deserializeBinaryFromReader(msg, reader);
};


/**
 * Deserializes binary data (in protobuf wire format) from the
 * given reader into the given message object.
 * @param {!proto.com.chicago.dto.Component} msg The message object to deserialize into.
 * @param {!jspb.BinaryReader} reader The BinaryReader to use.
 * @return {!proto.com.chicago.dto.Component}
 */
proto.com.chicago.dto.Component.deserializeBinaryFromReader = function(msg, reader) {
  while (reader.nextField()) {
    if (reader.isEndGroup()) {
      break;
    }
    var field = reader.getFieldNumber();
    switch (field) {
    case 1:
      var value = /** @type {string} */ (reader.readString());
      msg.setClassName(value);
      break;
    case 2:
      var value = /** @type {!proto.com.chicago.dto.Component.ComponentType} */ (reader.readEnum());
      msg.setResourceType(value);
      break;
    default:
      reader.skipField();
      break;
    }
  }
  return msg;
};


/**
 * Serializes the message to binary data (in protobuf wire format).
 * @return {!Uint8Array}
 */
proto.com.chicago.dto.Component.prototype.serializeBinary = function() {
  var writer = new jspb.BinaryWriter();
  proto.com.chicago.dto.Component.serializeBinaryToWriter(this, writer);
  return writer.getResultBuffer();
};


/**
 * Serializes the given message to binary data (in protobuf wire
 * format), writing to the given BinaryWriter.
 * @param {!proto.com.chicago.dto.Component} message
 * @param {!jspb.BinaryWriter} writer
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.com.chicago.dto.Component.serializeBinaryToWriter = function(message, writer) {
  var f = undefined;
  f = message.getClassName();
  if (f.length > 0) {
    writer.writeString(
      1,
      f
    );
  }
  f = message.getResourceType();
  if (f !== 0.0) {
    writer.writeEnum(
      2,
      f
    );
  }
};


/**
 * @enum {number}
 */
proto.com.chicago.dto.Component.ComponentType = {
  NONE: 0,
  EVENT_SCHEDULER: 1,
  EVENT_DISPATCHER: 2,
  ID_GEN_FACTORY: 3,
  GRPC_SERVER: 4,
  SYSTEM_STATE: 5,
  ASYNC_EXECUTOR: 6,
  JOB_MANAGER: 7
};

/**
 * optional string class_name = 1;
 * @return {string}
 */
proto.com.chicago.dto.Component.prototype.getClassName = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 1, ""));
};


/** @param {string} value */
proto.com.chicago.dto.Component.prototype.setClassName = function(value) {
  jspb.Message.setProto3StringField(this, 1, value);
};


/**
 * optional ComponentType resource_type = 2;
 * @return {!proto.com.chicago.dto.Component.ComponentType}
 */
proto.com.chicago.dto.Component.prototype.getResourceType = function() {
  return /** @type {!proto.com.chicago.dto.Component.ComponentType} */ (jspb.Message.getFieldWithDefault(this, 2, 0));
};


/** @param {!proto.com.chicago.dto.Component.ComponentType} value */
proto.com.chicago.dto.Component.prototype.setResourceType = function(value) {
  jspb.Message.setProto3EnumField(this, 2, value);
};



/**
 * Generated by JsPbCodeGenerator.
 * @param {Array=} opt_data Optional initial data array, typically from a
 * server response, or constructed directly in Javascript. The array is used
 * in place and becomes part of the constructed object. It is not cloned.
 * If no data is provided, the constructed object will be empty, but still
 * valid.
 * @extends {jspb.Message}
 * @constructor
 */
proto.com.chicago.dto.ComponentsConfig = function(opt_data) {
  jspb.Message.initialize(this, opt_data, 0, -1, proto.com.chicago.dto.ComponentsConfig.repeatedFields_, null);
};
goog.inherits(proto.com.chicago.dto.ComponentsConfig, jspb.Message);
if (goog.DEBUG && !COMPILED) {
  proto.com.chicago.dto.ComponentsConfig.displayName = 'proto.com.chicago.dto.ComponentsConfig';
}
/**
 * List of repeated fields within this message type.
 * @private {!Array<number>}
 * @const
 */
proto.com.chicago.dto.ComponentsConfig.repeatedFields_ = [1];



if (jspb.Message.GENERATE_TO_OBJECT) {
/**
 * Creates an object representation of this proto suitable for use in Soy templates.
 * Field names that are reserved in JavaScript and will be renamed to pb_name.
 * To access a reserved field use, foo.pb_<name>, eg, foo.pb_default.
 * For the list of reserved names please see:
 *     com.google.apps.jspb.JsClassTemplate.JS_RESERVED_WORDS.
 * @param {boolean=} opt_includeInstance Whether to include the JSPB instance
 *     for transitional soy proto support: http://goto/soy-param-migration
 * @return {!Object}
 */
proto.com.chicago.dto.ComponentsConfig.prototype.toObject = function(opt_includeInstance) {
  return proto.com.chicago.dto.ComponentsConfig.toObject(opt_includeInstance, this);
};


/**
 * Static version of the {@see toObject} method.
 * @param {boolean|undefined} includeInstance Whether to include the JSPB
 *     instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @param {!proto.com.chicago.dto.ComponentsConfig} msg The msg instance to transform.
 * @return {!Object}
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.com.chicago.dto.ComponentsConfig.toObject = function(includeInstance, msg) {
  var f, obj = {
    componentsList: jspb.Message.toObjectList(msg.getComponentsList(),
    proto.com.chicago.dto.Component.toObject, includeInstance)
  };

  if (includeInstance) {
    obj.$jspbMessageInstance = msg;
  }
  return obj;
};
}


/**
 * Deserializes binary data (in protobuf wire format).
 * @param {jspb.ByteSource} bytes The bytes to deserialize.
 * @return {!proto.com.chicago.dto.ComponentsConfig}
 */
proto.com.chicago.dto.ComponentsConfig.deserializeBinary = function(bytes) {
  var reader = new jspb.BinaryReader(bytes);
  var msg = new proto.com.chicago.dto.ComponentsConfig;
  return proto.com.chicago.dto.ComponentsConfig.deserializeBinaryFromReader(msg, reader);
};


/**
 * Deserializes binary data (in protobuf wire format) from the
 * given reader into the given message object.
 * @param {!proto.com.chicago.dto.ComponentsConfig} msg The message object to deserialize into.
 * @param {!jspb.BinaryReader} reader The BinaryReader to use.
 * @return {!proto.com.chicago.dto.ComponentsConfig}
 */
proto.com.chicago.dto.ComponentsConfig.deserializeBinaryFromReader = function(msg, reader) {
  while (reader.nextField()) {
    if (reader.isEndGroup()) {
      break;
    }
    var field = reader.getFieldNumber();
    switch (field) {
    case 1:
      var value = new proto.com.chicago.dto.Component;
      reader.readMessage(value,proto.com.chicago.dto.Component.deserializeBinaryFromReader);
      msg.addComponents(value);
      break;
    default:
      reader.skipField();
      break;
    }
  }
  return msg;
};


/**
 * Serializes the message to binary data (in protobuf wire format).
 * @return {!Uint8Array}
 */
proto.com.chicago.dto.ComponentsConfig.prototype.serializeBinary = function() {
  var writer = new jspb.BinaryWriter();
  proto.com.chicago.dto.ComponentsConfig.serializeBinaryToWriter(this, writer);
  return writer.getResultBuffer();
};


/**
 * Serializes the given message to binary data (in protobuf wire
 * format), writing to the given BinaryWriter.
 * @param {!proto.com.chicago.dto.ComponentsConfig} message
 * @param {!jspb.BinaryWriter} writer
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.com.chicago.dto.ComponentsConfig.serializeBinaryToWriter = function(message, writer) {
  var f = undefined;
  f = message.getComponentsList();
  if (f.length > 0) {
    writer.writeRepeatedMessage(
      1,
      f,
      proto.com.chicago.dto.Component.serializeBinaryToWriter
    );
  }
};


/**
 * repeated Component components = 1;
 * @return {!Array.<!proto.com.chicago.dto.Component>}
 */
proto.com.chicago.dto.ComponentsConfig.prototype.getComponentsList = function() {
  return /** @type{!Array.<!proto.com.chicago.dto.Component>} */ (
    jspb.Message.getRepeatedWrapperField(this, proto.com.chicago.dto.Component, 1));
};


/** @param {!Array.<!proto.com.chicago.dto.Component>} value */
proto.com.chicago.dto.ComponentsConfig.prototype.setComponentsList = function(value) {
  jspb.Message.setRepeatedWrapperField(this, 1, value);
};


/**
 * @param {!proto.com.chicago.dto.Component=} opt_value
 * @param {number=} opt_index
 * @return {!proto.com.chicago.dto.Component}
 */
proto.com.chicago.dto.ComponentsConfig.prototype.addComponents = function(opt_value, opt_index) {
  return jspb.Message.addToRepeatedWrapperField(this, 1, opt_value, proto.com.chicago.dto.Component, opt_index);
};


proto.com.chicago.dto.ComponentsConfig.prototype.clearComponentsList = function() {
  this.setComponentsList([]);
};


goog.object.extend(exports, proto.com.chicago.dto);
