@Grapes(
    @Grab(group='org.codehaus.groovy', module='groovy-xmlrpc', version='0.8')
)         
import groovy.net.xmlrpc.*

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
def partnerName = "Ricky Martin" //customerName
def partner_id = odoo.execute_kw(db, uid, pwd, 
                                'res.partner', 'search', 
                                [[['name', '=', partnerName]]])[0] //search: return a list with id(s)
println partnerName + " id: " + partner_id

///***** now search for sale orders of this partner with state 'draft'
def ordersId = odoo.execute_kw(db, uid, pwd, 
                               'sale.order', 'search', 
                               [[[ 'partner_id', '=', partner_id],
                                 [ 'state', '=', 'draft']]])      //search for quotations
println  partnerName + " orders in draft state : " + ordersId

///******** read the orders attributes ******** 
def data = odoo.execute_kw(db, uid, pwd, 
                           'sale.order', 'read', [ordersId], 
                           [fields:['name', 'state']]) 
println "Order details:"
data.each{it -> 
    println  "Trying to confirm order $it.id: $it.name:$it.state ..."
    idToConfirm = it.id
    odoo.execute_kw(db, uid, pwd,
                       'sale.order', 'action_confirm', [idToConfirm])
}
