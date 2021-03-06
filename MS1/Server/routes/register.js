var constants = require('../constants/constants.json');
var devicesFunction = require('../functions/devices');
var sendFunction = require('../functions/send-message');
var argon2 = require('argon2');
 
 //Funktion zum registrieren von Wähler und Kandidaten
 exports.register = function (db, redis) {
 	return function (req, res) {
 		console.log(req.body);

 		var typ = req.body.typ;
 		var emaildata = req.body.email;
		var username = req.body.username;
		var password = req.body.password;
		var wahlkreis = req.body.wahlkreis;
	
		if ( typeof emaildata  == 'undefined' || typeof username  == 'undefined' || typeof password  == 'undefined' || typeof typ  == 'undefined' || typeof wahlkreis  == 'undefined') {
			console.log(constants.error.msg_invalid_param.message);
			res.status(403).json(constants.error.msg_invalid_param).end();
 
        } else if ( !emaildata.trim()  || !username.trim() || !password.trim()  || !typ.trim() || !wahlkreis.trim()) {
			console.log(constants.error.msg_empty_param.message);
			res.status(403).json(constants.error.msg_empty_param).end();
 
        } else if (emaildata) {
 			//Überprüfung ob im SET "email" die geschickte EMAIL dabei ist
 			checkSET("email", emaildata).then(function (check) {
 				if (check == 1) {
 					console.log("Die email ist schon im set", emaildata);
 					res.status(409).end();
 				} else {
					//Überprüfung ob Username noch nicht vergeben
					checkSET("user", username) .then(function(check2) {
						// Username ist schon vorhanden
						if (check2 == 1) {
						console.log("Der Username ist schon vorhanden:", username);
						res.status(409).end();
						}else{
							//Passwort hashen
							argon2.generateSalt().then(salt => {
								argon2.hash(req.body.password, salt).then(hash => {
									console.log('Successfully created Argon2 hash:', hash);
									var pwhash = hash
							 
									//TYP = Waehler
									if (typ == "waehler") {
										
										var Waehler = {
											username: req.body.username,
											password: pwhash,
											wahlkreis: req.body.wahlkreis,
											email: req.body.email,
											WID: ""

										};
										
										var Client_JSON = {
											Client_ID: ""
										};
										
										//LETZTE ID WÄHLER
										var last_Waehler_ID;
										db.get('last_Waehler_ID', function (err, reply) {
											if (err)
												throw err;

											if (!reply || reply == "WID_NaN") {
												last_Waehler_ID = "WID_1";
											} else {
												last_Waehler_ID = reply.toString();
											}

											var old_WID = last_Waehler_ID.substring(4);
											var old_WID_INT = parseInt(old_WID);
											var new_WID = old_WID_INT + 1;
											var new_Waehler_ID = "WID_" + new_WID.toString();
											console.log("NEUE WID:  ", new_Waehler_ID);
											Waehler.WID = new_Waehler_ID;
											//Wähler in Redis speichern
											db.set(new_Waehler_ID, JSON.stringify(Waehler));
											//Email Adresse zum Set "email" hinzufügen
											db.SADD("email", emaildata);
											db.SADD("user", username);
											db.set(emaildata, new_Waehler_ID);
											db.set(username, new_Waehler_ID);
											//last_Wahler_ID wird aktuallierst
											db.set('last_Waehler_ID', new_Waehler_ID);
											Client_JSON.Client_ID = new_Waehler_ID;
											db.incr(wahlkreis+"_anzahlwaehler", function(err, reply){
												console.log("Anzahl Wähler in Wahlkreis " + wahlkreis +" :", reply);
											});
											res.status(201).send(Client_JSON).end();
											
										});

									}

									// TYP = KANDIDAT
									if (typ == "kandidat") {
										var new_Kandidat_ID;
										var Kandidat = {
											username: req.body.username,
											vorname: req.body.vorname,
											nachname: req.body.nachname,
											password: pwhash,
											wahlkreis: req.body.wahlkreis,
											email: req.body.email,
											KID: "",
											Partei: req.body.partei,
											Thesen_beantwortet: [],
											Begruendungen: [],
											Biografie: {
												Geburtsdatum: "",
												Bildungsweg: "",
												Berufe: "",
												Mitgliedschaften: ""
											},
											Wahlprogramm: {
												Webseite: "",
												Text: "",
												Link: ""
											}
										};							
										var Client_JSON = {
											Client_ID: ""
										};
										//LETZTE ID WÄHLER
										var last_Kandidat_ID;
										db.get('last_Kandidat_ID', function (err, reply) {
											if (err)
												throw err;

											if (!reply || reply == "KID_NaN") {
												last_Kandidat_ID = "KID_1";
											} else {
												last_Kandidat_ID = reply.toString();
											}

											var old_KID = last_Kandidat_ID.substring(4);
											var old_KID_INT = parseInt(old_KID);
											var new_KID = old_KID_INT + 1;
											new_Kandidat_ID = "KID_" + new_KID.toString();
											Kandidat.KID = new_Kandidat_ID;
											//Kandidat in Redis speichern
											db.set(new_Kandidat_ID, JSON.stringify(Kandidat));
											//Email Adresse zum Set "email" hinzufügen
											db.SADD("email", emaildata);
											db.SADD("user", username);
											db.SADD("Kandidaten_"+wahlkreis, new_Kandidat_ID);
											db.set(emaildata, new_Kandidat_ID);
											db.set(username, new_Kandidat_ID);
											//last_Kandidat_ID wird aktuallierst
											db.set('last_Kandidat_ID', new_Kandidat_ID); 						
											Client_JSON.Client_ID = new_Kandidat_ID;
											res.status(201).send(Client_JSON).end();
											db.incr(wahlkreis+"_anzahlkandidaten", function(err, reply){
												console.log("Anzahl Kandidaten in Wahlkreis " + wahlkreis+ " :", reply);
											});
											devicesFunction.listDevices(wahlkreis, db,  function(result) {
												sendFunction.sendMessage(new_Kandidat_ID, result, function (callback){
													console.log(callback);
												});
											});
										});

									} 
								});
							});
						}
					});
 				}
 			});
 		}
 	
	}

 function checkSET(set, data) {
 	var promises = [];
 	promises.push(db.SISMEMBERAsync(set, data));

 	return Promise.all(promises).then(function (arrayOfResults) {

 		return arrayOfResults[0];
 	});
 }

}