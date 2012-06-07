import com.framework.mongo.MongoDb


beans = {
	mongoDb(MongoDb) {
		enable = true
		hostname = "127.0.0.1"
	}
}
