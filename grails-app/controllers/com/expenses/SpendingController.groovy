package com.expenses

import org.bson.types.ObjectId

import com.framework.mongo.MongoDb

import grails.converters.JSON


/**
 * @author bhaslop
 *
 */
class SpendingController {

	MongoDb mongoDb

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
}