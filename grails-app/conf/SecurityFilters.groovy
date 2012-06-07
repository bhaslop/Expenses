
class SecurityFilters {
	def filters = {
		loginCheck(controller:'*', action: '*', actionExclude:'login') {
			before = {
				if( !session.user )
					println 'not logged in'
				else
					println session.user
			}
		}
	}
}
