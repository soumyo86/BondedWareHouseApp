///<reference path = "angular.min.js" />

var myApp = angular
					.module("myModule",[])
					.controller("myController",function($scope, $http, $interval, $log, $window){
						$scope.initMap = function(deviceid, docID){
							 $window.scrollTo(0, 0);
							 document.getElementById('light').style.display='block';
							 document.getElementById('fade').style.display='block';
							 document.getElementById('loadingIcon').style.display='block';
							 document.getElementById('truckStatus').style.display='none';
							 $http({
                                 method:'get',
                                 url:'https://cbdocbatchservice.eu-gb.mybluemix.net/service/getTruckStatus/'+docID
							 	})
								 .then(function(response){
								                 console.log("truck status:"+response.data);
												 if(response.data.status != '')
												 {
													 var mapText = response.data.status;
												 }
												 else
												 {
													 var mapText = 'Truck not started yet'; 
												 }
								                 $scope.callMap(deviceid, mapText);
								 });
								 }
								 $scope.callMap = function(deviceid, mapText){
							$http({
								method:'get',
								url:'https://iotlocationtracker.mybluemix.net/truckTrack.php?truckID='+deviceid
							})
						.then(function(response){
							document.getElementById('loadingIcon').style.display='none';
								var myLatLng = {lat: parseFloat(response.data.lat), lng: parseFloat(response.data.lon)};
								console.log("Truck location:"+myLatLng);
                                if(response.data.lat != null){
                                	var map = new google.maps.Map(document.getElementById('mappop'), {
      								  zoom: 16,
      								  center: myLatLng
      								});

      								var marker = new google.maps.Marker({
      								  position: myLatLng,
      								  animation: google.maps.Animation.BOUNCE,
      								  map: map,
      								  title: deviceid
      								});
									document.getElementById('truckStatus').style.display='block';
									if(mapText == 'Arriving Custom')
										{
											document.getElementById("truckStatus").innerHTML = '<img title="High Alert" style="width:20px; height:20px; margin-top:-2px" src="./resources/img/alert.gif"/>  Truck Status: '+mapText;
										}
									else
										{
											document.getElementById("truckStatus").innerHTML = 'Truck Status: '+mapText;
										}
                                }else{
                                	 document.getElementById('noData').style.display='block';
                                	 document.getElementById('truckStatus').style.display='none';
                                }
								
						});
						}
						
						$scope.StopTimer = function () {
							var iEl = angular.element( document.querySelector( '#mappop' ) );
						     iEl.html('');
							document.getElementById('light').style.display='none';
							document.getElementById('fade').style.display='none';
							document.getElementById('noData').style.display='none';
						};
						
					});

