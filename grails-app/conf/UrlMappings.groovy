class UrlMappings {

	static mappings = {
		"/spending/search/concept/$concept" ( controller: "spending" ) {
			action = [ GET: "searchconcept" ]
		}
		
		"/spending" ( controller: "spending" ) {
			action = [ POST: "insert" ]
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
