import com.framework.mongo.MongoDb

beans = {
	mongoDb(MongoDb) {
		enable = true
		hostname = "localhost"
	}
}
