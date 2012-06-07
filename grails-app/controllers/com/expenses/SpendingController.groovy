package com.expenses

import org.bson.types.ObjectId

import com.framework.mongo.MongoDb
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson.JacksonFactory

import grails.converters.JSON


/**
 * @author bhaslop
 *
 */
class SpendingController {

	MongoDb mongoDb
	
	HttpTransport HTTP_TRANSPORT = new NetHttpTransport()
	JsonFactory JSON_FACTORY = new JacksonFactory()
	
	
	def index() {
		
	}

	def searchconcept() {
		def search = [:]

		if( !params.concept )
			render (status: 404)

		search["concept"] = params.concept

		def resp = mongoDb.query("spending", search)

		render resp as JSON
	}

	def insert() {
		def spending = request.JSON
		
		spending._id = new ObjectId()
		spending.date = new Date()
		
		mongoDb.insert("spending", spending)	

		render spending as JSON
	}
	
	def test() {
		GoogleCredential credential = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
		.setJsonFactory(JSON_FACTORY)
		.setServiceAccountId("1085995914959@developer.gserviceaccount.com")
		.setServiceAccountScopes("https://docs.google.com/feeds/")
		.setServiceAccountPrivateKeyFromP12File(new File("credentials/9085dcadffb072846f134f9440cd9c959e1b4aad-privatekey.p12"))
		// .setServiceAccountUser("user@example.com")
		.build();
		
		println credential.getAccessToken();
		
		render [:] as JSON
	}
}