@Grapes(
    @Grab(group='org.codehaus.groovy', module='groovy-xmlrpc', version='0.8')
)         
import groovy.net.xmlrpc.*

//////  Retrieve the list of corporate customers: 
//////  res.partner with customer=true and is_company = true

//Configuration des accès au serveur
def myserver = "YOUR SERVER ADDRESS"
def username = "USERNAME"
def pwd = "PASSWORD"
def db = "DATABASE"
def common = new XMLRPCServerProxy(myserver+'common')
def uid = common.authenticate(db, username, pwd, [])
def odoo =  new XMLRPCServerProxy(myserver+'object')


def common = new XMLRPCServerProxy(myserver+'common')
def uid = common.authenticate(db, username, pwd, [])

def odoo =  new XMLRPCServerProxy(myserver+'object')

///******* search partner id of a given partner ******** 
def partnerName = "Ricky Martin"

def partner_id = odoo.execute_kw(db, uid, pwd, 
                                'res.partner', 'search', 
                                [[['name', '=', partnerName]]])[0] //search: return a list with id(s)
println partnerName + " id: " + partner_id

///***** now search for sale orders of this partner
def ordersId = odoo.execute_kw(db, uid, pwd, 
                               'sale.order', 'search', 
                               [[[ 'partner_id', '=', partner_id]]]) //search: return a list with id(s)
println  partnerName + " orders : " + ordersId

///******** read the orders attributes ******** 
def data = odoo.execute_kw(db, uid, pwd, 
                           'sale.order', 'read', [ordersId], 
                           [fields:['name', 'state', 'amount_untaxed']]) 
println "Order details:"
data.each{
	println data.state[0]
	/*println  "Order $it.id: $it.name:$it.state CHF $it.amount_untaxed (Hors TVA)"*/
	/*println "Unavailable"*/
	/*it -> 
	
*/

}