@Grapes(
    @Grab(group='org.codehaus.groovy', module='groovy-xmlrpc', version='0.8')
)         
import groovy.net.xmlrpc.*
// import de la classe permettant d'utiliser XMLRPC

//Configuration des accès au serveur
def myserver = "YOUR SERVER ADDRESS"
def username = "USERNAME"
def pwd = "PASSWORD"
def db = "DATABASE"
def common = new XMLRPCServerProxy(myserver+'common')
def uid = common.authenticate(db, username, pwd, [])
def odoo =  new XMLRPCServerProxy(myserver+'object')

// Requête vers la base de données d'Odoo
// Recherche de tous les clients (customer) identifiés comme entreprise (is_company=true)
def args = [[['customer', '=', true], 
             ['is_company', '=', true]]]
def partnerIds = odoo.execute_kw(db, uid, pwd, 'res.partner', 'search', args) 
// PartnerIds recupère la liste des ID résultant de la requête précédente

println "I received " + partnerIds.size() + " of them."

//Récupération des données et sauvegarde dans la variable data
def data = odoo.execute_kw(db, uid, pwd, 
                           'res.partner', 'read', [partnerIds], 
                           [fields:['name', 'supplier', 'lang']]) 
//def i=1;
//Boucle permettant de parcourir et d'afficher les informations stockées dans la variable data
data.each{
    it -> println "$it.id : $it.name, speak $it.lang, also suppliers:$it.supplier"
    //i++;
}