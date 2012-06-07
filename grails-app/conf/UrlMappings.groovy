class UrlMappings {
	static mappings = {
		"/spending/search/concept/$concept" ( controller: "spending" ) {
			action = [ GET: "searchconcept" ]
		}
		
		"/spending" ( controller: "spending" ) {
			action = [ POST: "insert" ]
		}
		
		
		"/test" (controller: "spending" ) {
			action = [ GET: "test" ]
		}
		
		"/$controller/$action?/$id?" {
			constraints {
				// apply constraints here
			}
		}

		"/"(view:"/index")
		"500"(view:'/error')
	}
}