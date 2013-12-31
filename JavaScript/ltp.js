require("./util.js");
var http = require("http");
var parseString = require("xml2js").parseString;

// Class Service
exports.Service = function(au, encoding) {
    this.au = au;
    this.addr = "api.ltp-cloud.com";
    this.port = 8080;
    this.uris = "/ltp_srv/ltp";
    this.encoding = encoding !== undefined ? encoding : "utf-8"
}

exports.Service.prototype = {
    analyse: function(input, option, callback) {
        var result = "";
        var data = "s=" + input + 
                   "&x=" + "n" +
                   "&c=" + this.encoding +
                   "&t=" + (option !== undefined ? option : "all");
        var options = {
            hostname : this.addr,
            port : this.port,
            path : this.uris,
            method : "POST",
            headers : {
                "Content-length" : Buffer.byteLength(data, "utf-8"),
                "Authorization" : "Basic " + new Buffer(this.au).toString("base64").replace("\n", "")
            }
        };

        var req = http.request(options, function(res) {
            res.setEncoding("utf8");
            res.on("data", function(chunk) {
                result += chunk;
            });
            res.on("end", function() {
                parseString(result, function(err, res) {
                    if (err) {
                        console.log(err);
                    }
                    callback(res);
                })
            });
        });

        req.on("error", function(e) {
            console.log("Problem with request: " + e.message);
        });

        req.end(data);
    }
} 
// End of class Service