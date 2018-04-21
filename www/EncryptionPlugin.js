var exec = require('cordova/exec');
var encryption = function(){};  

encryption.prototype.coolMethod = function(arg0, success, error) {
    exec(success, error, "EncryptionPlugin", "coolMethod", arg0);
};

var Encryption = new encryption();
module.exports = Encryption;
