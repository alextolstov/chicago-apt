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

goog.exportSymbol('proto.com.chicago.dto.Customer', null, global);

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
proto.com.chicago.dto.Customer = function(opt_data) {
  jspb.Message.initialize(this, opt_data, 0, -1, null, null);
};
goog.inherits(proto.com.chicago.dto.Customer, jspb.Message);
if (goog.DEBUG && !COMPILED) {
  proto.com.chicago.dto.Customer.displayName = 'proto.com.chicago.dto.Customer';
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
proto.com.chicago.dto.Customer.prototype.toObject = function(opt_includeInstance) {
  return proto.com.chicago.dto.Customer.toObject(opt_includeInstance, this);
};


/**
 * Static version of the {@see toObject} method.
 * @param {boolean|undefined} includeInstance Whether to include the JSPB
 *     instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @param {!proto.com.chicago.dto.Customer} msg The msg instance to transform.
 * @return {!Object}
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.com.chicago.dto.Customer.toObject = function(includeInstance, msg) {
  var f, obj = {
    phone: jspb.Message.getFieldWithDefault(msg, 1, ""),
    firstname: jspb.Message.getFieldWithDefault(msg, 2, ""),
    middlename: jspb.Message.getFieldWithDefault(msg, 3, ""),
    lastname: jspb.Message.getFieldWithDefault(msg, 4, "")
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
 * @return {!proto.com.chicago.dto.Customer}
 */
proto.com.chicago.dto.Customer.deserializeBinary = function(bytes) {
  var reader = new jspb.BinaryReader(bytes);
  var msg = new proto.com.chicago.dto.Customer;
  return proto.com.chicago.dto.Customer.deserializeBinaryFromReader(msg, reader);
};


/**
 * Deserializes binary data (in protobuf wire format) from the
 * given reader into the given message object.
 * @param {!proto.com.chicago.dto.Customer} msg The message object to deserialize into.
 * @param {!jspb.BinaryReader} reader The BinaryReader to use.
 * @return {!proto.com.chicago.dto.Customer}
 */
proto.com.chicago.dto.Customer.deserializeBinaryFromReader = function(msg, reader) {
  while (reader.nextField()) {
    if (reader.isEndGroup()) {
      break;
    }
    var field = reader.getFieldNumber();
    switch (field) {
    case 1:
      var value = /** @type {string} */ (reader.readString());
      msg.setPhone(value);
      break;
    case 2:
      var value = /** @type {string} */ (reader.readString());
      msg.setFirstname(value);
      break;
    case 3:
      var value = /** @type {string} */ (reader.readString());
      msg.setMiddlename(value);
      break;
    case 4:
      var value = /** @type {string} */ (reader.readString());
      msg.setLastname(value);
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
proto.com.chicago.dto.Customer.prototype.serializeBinary = function() {
  var writer = new jspb.BinaryWriter();
  proto.com.chicago.dto.Customer.serializeBinaryToWriter(this, writer);
  return writer.getResultBuffer();
};


/**
 * Serializes the given message to binary data (in protobuf wire
 * format), writing to the given BinaryWriter.
 * @param {!proto.com.chicago.dto.Customer} message
 * @param {!jspb.BinaryWriter} writer
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.com.chicago.dto.Customer.serializeBinaryToWriter = function(message, writer) {
  var f = undefined;
  f = message.getPhone();
  if (f.length > 0) {
    writer.writeString(
      1,
      f
    );
  }
  f = message.getFirstname();
  if (f.length > 0) {
    writer.writeString(
      2,
      f
    );
  }
  f = message.getMiddlename();
  if (f.length > 0) {
    writer.writeString(
      3,
      f
    );
  }
  f = message.getLastname();
  if (f.length > 0) {
    writer.writeString(
      4,
      f
    );
  }
};


/**
 * optional string phone = 1;
 * @return {string}
 */
proto.com.chicago.dto.Customer.prototype.getPhone = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 1, ""));
};


/** @param {string} value */
proto.com.chicago.dto.Customer.prototype.setPhone = function(value) {
  jspb.Message.setProto3StringField(this, 1, value);
};


/**
 * optional string firstName = 2;
 * @return {string}
 */
proto.com.chicago.dto.Customer.prototype.getFirstname = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 2, ""));
};


/** @param {string} value */
proto.com.chicago.dto.Customer.prototype.setFirstname = function(value) {
  jspb.Message.setProto3StringField(this, 2, value);
};


/**
 * optional string middleName = 3;
 * @return {string}
 */
proto.com.chicago.dto.Customer.prototype.getMiddlename = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 3, ""));
};


/** @param {string} value */
proto.com.chicago.dto.Customer.prototype.setMiddlename = function(value) {
  jspb.Message.setProto3StringField(this, 3, value);
};


/**
 * optional string lastName = 4;
 * @return {string}
 */
proto.com.chicago.dto.Customer.prototype.getLastname = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 4, ""));
};


/** @param {string} value */
proto.com.chicago.dto.Customer.prototype.setLastname = function(value) {
  jspb.Message.setProto3StringField(this, 4, value);
};


goog.object.extend(exports, proto.com.chicago.dto);
