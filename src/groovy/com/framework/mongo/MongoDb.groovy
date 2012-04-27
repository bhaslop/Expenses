package com.framework.mongo

import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.springframework.beans.factory.InitializingBean

import com.framework.utils.GeneralUtils
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import com.mongodb.Mongo
import com.mongodb.MongoOptions
import com.mongodb.WriteConcern

class MongoDb implements InitializingBean {
	Mongo mongo
	String dbName = "expenses"
	
	boolean enable
	String hostname
	int port = 27017

	def maxWaitTime = 50
	def connectTimeout = 50
	def socketTimeout = 20

	private static final log = LogFactory.getLog(this)

	static DEFAULT_OPTIONS = [failOnError: true, timeout: 0]

	void afterPropertiesSet() {
		if(!enable) return

		def config = ApplicationHolder.application.config

		def mongoOptions = new MongoOptions()
		mongoOptions.connectionsPerHost = config.mongo.connectionsPerHost

		mongoOptions.connectTimeout = connectTimeout
		if(config.global.scope == 'read') {
			mongoOptions.maxWaitTime = maxWaitTime
			mongoOptions.socketTimeout = socketTimeout
		}

		mongo = new Mongo("$hostname:$port", mongoOptions)
		System.out.println("done loading mongo");
		System.out.println(mongo);
	}

	Collection query(String collectionName, Map map, Map args = [:]) {
		Map options = DEFAULT_OPTIONS.clone()
		options.putAll(args)

		System.out.println(map);

		try {
			log.debug { "Querying $map" }

			return GeneralUtils.callWithTimeout(options.timeout) {
				def cursor = mongo.getDB(dbName).getCollection(collectionName).find(new BasicDBObject(map))

				def result = []
				while (cursor.hasNext()) {
					DBObject object = cursor.next()
					result << object.toMap()
				}

				result
			}

		} catch (e) {
			if(e.cause in SocketTimeoutException) {
			} else {
				if(options.failOnError) throw e
				log.error "Error querying mongo", e
			}

			return []
		}
	}

	void upsert(String collectionName, Map query, Map object, Map args = [:]) {
		Map options = DEFAULT_OPTIONS.clone()
		options.putAll(args)

		GeneralUtils.callWithTimeout(options.timeout) {
			mongo.getDB(dbName).getCollection(collectionName).update(new BasicDBObject(query), new BasicDBObject(object), true, false, options.writeConcern?: WriteConcern.SAFE)
		}

		log.debug { "Mongo upsert id $id: $object" }
	}

	void insert(String collectionName, Map object, Map args = [:]) {
		System.out.println("sarlanga");
		Map options = DEFAULT_OPTIONS.clone()
		options.putAll(args)

		System.out.println(mongo);
		GeneralUtils.callWithTimeout(options.timeout) {
			mongo.getDB(dbName).getCollection(collectionName).insert(new BasicDBObject(object), options.writeConcern?: WriteConcern.SAFE)
		}
		System.out.println("Inserted: " + object);
		log.debug { "Mongo insert: $object" }
	}

	void delete(String collectionName, Map object, Map args = [:]) {
		Map options = DEFAULT_OPTIONS.clone()
		options.putAll(args)

		GeneralUtils.callWithTimeout(options.timeout) {
			mongo.getDB(dbName).getCollection(collectionName).remove(new BasicDBObject(object), options.writeConcern?: WriteConcern.NORMAL)
		}

		log.debug { "Mongo delete: $id" }
	}
}
