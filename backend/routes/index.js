var express = require('express');
var router = express.Router();
var fb = new Firebase("https://photoupload.firebaseio.com/");

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

/**
{ name: "Will", age: 18, url: "http://asdfasdfasfd" }
name -> req.body.name
age -> req.body.age
*/

/**

{ url: "Http.asdfasdf", date: "asdfafd"}

var family = {
    "jason" : {
        "name" : "Jason Lengstorf",
        "age" : "24",
        "gender" : "male"
    },
    "kyle" : {
        "name" : "Kyle Lengstorf",
        "age" : "21",
        "gender" : "male"
    }
}
*/

router.post('/', function(req, res, next) {
    for (key in req.body) {
        fb.set({image: req.body.key.url, date: req.body.key.date});
    }
    res.status(200).send("Ok");
    // var img1 = asdfasfdaf req.body
    // ...
    // Firebase.save(0asdfasdf)
    // ...
    // res.status(200).send("Ok");
});

module.exports = router;
