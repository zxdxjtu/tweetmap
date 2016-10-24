<!DOCTYPE html>
<html>
<head>
    <title>Tweet Map</title>
    <meta charset="utf-8">
    <meta name="viewport" content="initial-scale=1.0">
    <style>
        html, body {
            height: 100%;
        }
        #floating-panel {
            top: 50px;
            right: 50px;
            position: absolute;
            z-index: 1000;
        }
        #map {
            height: 100%;
        }
        #coord {
            padding: 5px;
            color: white;
            background-color: black;
        }
        #dropdownMain {
            width: 90px;
            color: aquamarine;

        }
    </style>

    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.1.8/semantic.min.css">
</head>

<body>
<div id="floating-panel">
    <div class="dropdown">
            <button type="button" class="btn btn-danger dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Choose Keyword
                <span class="caret"></span>
                <span class="sr-only">Toggle Dropdown</span>
            </button>
            <ul class="dropdown-menu">
                <li><a href="javascript:showMark('job');">Job</a></li>
                <li><a href="javascript:showMark('game');">game</a></li>
                <li><a href="javascript:showMark('love');">Love</a></li>
                <li><a href="javascript:showMark('food');">Food</a></li>
                <li><a href="javascript:showMark('fashion');">Fashion</a></li>
                <li><a href="javascript:showMark('NewYork');">NewYork</a></li>
                <li><a href="javascript:showMark('LOL');">LOL</a></li>
                <li><a href="javascript:showMark('Trump');">Trump</a></li>
                <li><a href="javascript:showMark('Hilary');">Hilary</a></li>
                <li><a href="javascript:showMark('hello');">hello</a></li>
            </ul>
    </div>
</div>

<div id="map"></div>
<div id="coord"></div>

<script>
     
     var markers = [];
     var infowindows = [];
     var center = {lat: 40.8075, lng: -73.962};
     var map;
	 var keywordjs = null;	
     
     function initMap() {
          map = new google.maps.Map(document.getElementById('map'), {
            center: center,
            zoom: 3
          });

          setInterval(function(){showMark1(keywordjs);}, 10000);
     }

     function newMap(origin) {
            map = new google.maps.Map(document.getElementById('map'), {
              center: origin.center,
              zoom: origin.zoom
            });
     }  

     function showMark(keyword){
    	 keywordjs = keyword;
    	 newMap(map);
    	 showMark1(keyword);
     }
     
     function showMark1(keyword) {
    	 console.log("showmarking");
         markers = [];
         $.ajax({
             url: "newServlet",
             type: "GET",
             contentType:"json",
             data: {
            	  key: keyword,
            	  },
            	  success: function( data ) {
            		  var edata = JSON.parse(data)
            		  console.log(edata.length);
            		  for(var t=0; t<edata.length; t++){
            		  	  var location ={lat: edata[t].lati, lng: edata[t].longi}; 
            	          var marker = new google.maps.Marker({
            		            position:location,
            		            map:map,
            		  		});
            		        markers.push(marker);
            			}
            		  },
            		error:function(data,status,er) {
            		   }
         });
      }
     
  
    </script>

<script  async defer 
src="http://maps.google.com/maps/api/js?key=YOUR_GOOGLEAPI_KEY&callback=initMap"></script>
</body>
</html>