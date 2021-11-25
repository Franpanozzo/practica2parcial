------------PARCIAL QMP---------------

@MappedSuperclass
public abstract class EntidadPersistente {

  @Id
  @GeneratedValue
  private long Id;
}

@Entity
class Usuario extends EntidadPersistente {
    @OneToMany
    @JoinColumn(name="usuario_id")
    guardarropas
}

@Entity
class Prenda extends EntidadPersistente {

    descripcion

    @Enumerated
    categoria

    @OneToMany
    @JoinColumn(name = "prenda_id")
    colores
}

@Entity
class Color {
    descripcion
    codigoHexa
}

@Embedable
class Ciudad {
    key
}

//Como solo el evento recurrente tiene un atributo (tipo de recurrencia) la hago single table
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)  
@DiscriminatorColumn(name = "tipoRecu", length = 1)
class Evento extends EntidadPersistente {
    @ManyToOne
    usuario

    inicio
    fin

    @Embedded
    ciudad
}

@Entity
@DiscriminatorValue("U")
class EventoUnico extends Evento {
    //logica
}

@Entity
@DiscriminatorValue("R")
class EventoRecurrente extends Evento {
    @Enumerated
    recurrencia
}

enum Recurrencia {
    SEMANAL-MENSUAL //cada una con su abstract method implementado
}

//ClienteApiClima al ser una API externa no lo persisto, tampoco las sugerencias que son 
//algo que se crean on the fly


------------PARCIAL GAME OF THRONES---------------

@MappedSuperclass
public abstract class EntidadPersistente {

  @Id
  @GeneratedValue
  private long Id;
}

@Entity
class Region extends EntidadPersistente implements Fundable{
    nombre

    @ManyToMany
    lugares

    @ManyToOne
    casaPrincipal
}

/*Aplico mapeo de herencia JOINED ya que hay un reparto equitativo de 
atributos, tanto en superclasescomo en subclases, ademas de que al no
hacerse consultas polimorficas la eligo por encima de SINGLE_TABLE
la cual es mejor para esto */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)  
class Lugar extends EntidadPersistente {
    nombre
    añoFundacion
    poblacion
}

@Entity
class Castillo extends Lugar {
    cantidadTorres
    cantidadMurallas
}

@Entity
class Ciudad extends Lugar {
    cantidadDeComercios
    cantidadDeSantuarios
    tasaDeMortalidad
}

@Entity
class Casa extends EntidadPersistente implements Fundable {
    nombre
    patrimonio
    añoFundacion

    @ManyToOne
    origen

    @OneToMany
    @JoinColumn(name = "casa_Id")
    fuerzaMilitar

    @ManyToOne
    casaVasalla
}

/*Paso la interfaz FuerzaMilitar a clase abstracta ya que las 
subclases no son stateless, e implemento el mapeo de herencia
SINGLE_TABLE ya que se hacen consultas polimorficas ya que la casa
conoce a una lista de estas, ademas de que a lo sumo te quedan 2 atributos
en null nomas y evito JOINS solamente por 1 campo*/

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) 
@DiscriminatorColumn(name = "tipo", length = 1)
abstract class FuerzaMilitar extends EntidadPersistente {
}

@DiscriminatorValue("A")
class Area extends FuerzaMilitar {
    cantidadDragones
}

@DiscriminatorValue("N")
class Naval extends FuerzaMilitar {
    cantidadBarcos
}

@DiscriminatorValue("T")
class Terrestre extends FuerzaMilitar {
    cantidadSoldados
}

/*Ninguna de las colecciones de este problema
 necesita soportar orden (ninguna es una List), por lo que no utilizamos @OrderColumn's

 @Enumerated es útil cuando tenemos strategies stateless. Pero las fuerzas militares son strategies stateful
 , por tanto, no podemos convertirlos en una enumeración de códigos, y necesitaremos una tabla para almacenar
  el estado extra.  */



--------------PARCIAL MUERTOS ANDANTES----------------

A)

@MappedSuperclass
public abstract class EntidadPersistente {
    @Id
    @GeneratedValue
    private long Id;
}

@Entity
class Grupo extends EntidadPersistente {
    nombre

    @OneToMany
    @JoinColumn(name = "grupo_id")
    integrantes

    @OneToOne
    lider
}

/*Uso single table ya que se van a hacer consultas plimorficas ya que el grupo conoce a varios
integrantes, ademas de que solo hay una subclase la cual no tiene atributos propios, por lo tanto
seria tener un join traerme un ID nomas, perdiendo en performance
*/
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) 
@DiscriminatorColumn(name = "tipo", length = 1)
/*@DiscriminatorValue("S") Por si solo se instancia un sobreviviente, ya que no es abstract class
MAL: Si esta en NULL el tipo quiere decir que es un sobreviviente nomas y ya esta */
class Sobreviviente extends EntidadPersistente {
    carismas
    puntos
    resistencia

    @Enumerated
    estado

    @ManyToMany
    @OrderColumn(name = "orden")
    armas
}

enum Estado {
    DIFERENTES ESTADOS

    METODOS ABSTRACTOS superarLaComplejidad y extraPorTomarse
}

@Entity
@DiscriminatorValue("P")
class Predador extends Sobreviviente {
    @ManyToMany
    caminantes
}

@Entity
class Caminante extends EntidadPersistente {
    sedDeSangre
    somnoliento
    dientes
}

/* Tambien elijo mapear esta herencia con SINGLE_TABLE ya que aunque no se hagan consultas polimorficas
ni se compartan atributos en comun veo innecesario crear tablas adicionales las cuales tendrian muy pocos
o ningun atributo, por lo tanto eligo single table. A lo sumo me quedan 3 atributos
en NULL lo cual no afecta. PODRIA SER TABLE_PER_CLASS TAMBIEN, SE DA TODO PARA QUE LO SEA
MENOS QUE TIENEN POCOS ATRIBUTOS
*/

/* 
*/

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) 
@DiscriminatorColumn(name = "tipo", length = 1)
abstract class Lugar extends EntidadPersistente {
    @OneToMany
    @JoinColumn(name= "lugar_id")
    caminantes
}

@Entity
@DiscriminatorColumn("P")
class Prision extends EntidadPersistente {
    cantidadPabellones
}

@Entity
@DiscriminatorColumn("B")
class Bosque extends EntidadPersistente {
}

@Entity
@DiscriminatorColumn("G")
class Granja extends EntidadPersistente {
    cantidadDeElementosPunzantes
    cantidadDeCercas
}

@Entity
class Armas extends EntidadPersistente {
    municiones
    calibre
    ruidosa
}

B)

. GET /sobrevivientes
    - query params(cantidad 'n', tipo)
    - ejemplo:
        /sobreviviente?cantidad=7&tipo=predador

. POST sobrevivientes/:id/armas


--------------PARCIAL REKOMENDASHI----------------

A)
1)

@MappedSuperclass
public abstract class EntidadPersistente {
    @Id
    @GeneratedValue
    private long Id;
}

@Entity
class Pedido extends EntidadPersistente {
    @EllementCollection
    @Enumerated
    coccionesPreferidas

    @EllementCollection
    @OrderColumn(name = "orden")
    @Enumerated
    caregoriasPreferidas

    @ManyToOne
    tipoDePedido
}

/*Paso la interfaz TipoDePedido a clase abstracta ya que las 
subclases tienen estado (strategy statefull), e implemento el mapeo de herencia
SINGLE_TABLE ya que se hacen consultas polimorficas ya que el pedido
conoce a esta, ademas de que a lo sumo te quedan 2 atributos
en null y evito JOINS solamente por 1 o 2 campos*/

@Entity 
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) 
@DiscriminatorColumn(name = "tipo", length = 1)
abstract class TipoDePedido extends EntidadPersistente {
}

@Entity
@DiscriminatorValue("I")
class Individual extend TipoDePedido {
    precioMaximo
    coeficienteDeVariedad
}

@Entity
@DiscriminatorValue("G")
class Grupal extend TipoDePedido {
    cantidadDeComensales
}

//CatalogoDePiezas es un servicio, por lo tanto no lo persisto
//@Entity 
//EL SET TAMPOCO SE PERSISTIRIA PORQUE COMO LAS SUGERENCIAS, SE CREAN ON THE FLY
class Set extends EntidadPersistente {
    //Porque no una seleccion podria estar en varios sets
    //@ManyToMany
    selecciones
}

@Entity
class Seleccion extends EntidadPersistente {
    cantidad

    @Embedded 
    pieza
}

//Embebo la clase pieza a la seleccion ya que es un Value Object ahorrandome un Join

@Embeddable //MALLLL, SOLO SE EMBEBE OneToOne
class Pieza {
    nombre
    precio
    imagen
    
    @Enumerated
    tipoCoccion

    /*Ingrediente tambien es un Value Object pero lo dejo como OneToMany para que trate de 
    manera mas optima los update, ya que si pongo embadble, elementCollection cuando actualize
    borra todo y lo vuelve a insertar. Alfinal, termino haciendo la relacion ManyToMany porque
    podria ser que un ingrediente este en varias piezas whynot?*/

    @ManyToMany
    ingredientes
}

@Entity
class Ingrediente extends EntidadPersistente {
    nombre
    @Enumerated
    caregoria
}

2)
La decision que voy a tomar es la de materializar esta dependencia temporal, ya que las mas 
pedidas van a ir variando y podria hacer una tabla la cual contenga:

---masPedidas---           La pieza estaria embebida
nombre
precio
imagen
diaSemana
posicion    //Su posicion en el top 10


De esta manera quedarian precalculadas las piezas mas pedidas en el caso de que se quieran usar
en el algoritmo de recomendar. Tengo en una tabla todo lo necesario para calcular


B)
Primero una pantalla donde puedas elegir tu categoria preferida
- Ingresar categoria y coccion preferida: GET /preferencias   Devuelve todas las categorias, cocciones  para elegir 
Request que se manda: POST /pedidos   Un nuevo pedido
                        body 
                            { categorias
                            cocciones }

Se guarda y se redirecciona a elegir el tipo de pedido

GET /tiposPedidos  //MEJOR PONER: GET /pedidos/tipos

metodo mas correcto ya que se modifica parcialmente el pedido
request: PATCH /pedidos/:id  //o PUT (NO UPDATE) tambien podria ser pero solo se modifican ciertos campos

Por limitacion de formulario: POST /pedido/:id

Redirecciona a mostrar el set recomendado

GET /pedidos/:id/setRecomendado


--------------PARCIAL GINPASS----------------

A)

@MappedSuperclass
public abstract class EntidadPersistente {
    @Id
    @GeneratedValue
    private long Id;
}

/*No persisto la busqueda ya que como dice el enunciado no es importante consultar todavia,
ademas de que va a ser algo que se va a crear On The Fly, por lo tanto no lo persisto*/

@Entity
class Suscripcion extends EntidadPersistente {
    cuotaMinima
    cuotaMaxima

    @ManyToMany
    localesIncluidos
}

@Entity
class Usuario extends EntidadPersistente {
    @ManyToOne
    suscrpcion

    @Embedded
    direccion
}

/*Embebo la direccion ya que al ser un value object, es una clase concreta y hay una relacion 
OneToOne, lo embebo para ahorrarme un JOIN, ademas de que capaz se quiera tener una copia
historica del domicilio de los clientes */

@Embeddable
class Direccion {
    calle
    altura
    longitud
    latitud
}

/* Debido a que se hacen consultas polimorficas ya que la suscripcion conoce a muchos locales,
ademas las subclases no tienen ningun campo por lo que crearia tablas que tengan un ID nomas
, por lo tanto es preferible ahorrare eso y los JOINS que trae otro tipo de estrategia
*/

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) 
@DiscriminatorColumn(name = "tipo", length = 1)
class Local extends EntidadPersistente {
    nombre

    @Embedded
    direccion
}

@Entity
@DiscriminatorValue("P")
class Pub extends Local {
    @ManyToMany
    @JoinTable(name = "pub_tragos")
    tragos
}

@Entity
@DiscriminatorValue("B")
class Bar extends Local {
    @ManyToMany
    @JoinTable(name = "bar_cervezas")
    cervezas 

    @ManyToMany
    @JoinTable(name = "bar_analocholicas")
    analcoholicas

    @ManyToMany
    @JoinTable(name = "bar_tragos")
    tragos
}

@Entity
@DiscriminatorValue("C")
class Cervecería extends Local { 
    @ManyToMany
    @JoinTable(name = "cerveceria_cervezas")
    cervezas
}

/*Tambien aplico SINGLE_TABLE ya que solo la subclase de cerveza tiene un atributo, entonces a lo
sumo queda un atributo en NULL, y es ideal para consultas polimorficas ya que la busqueda
va a tener una lista de bebidas */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) 
@DiscriminatorColumn(name = "tipo", length = 1)
class Bebida extends EntidadPersistente {
    nombre

    @Transient
    alcoholService  //Al ser un servicio no se persiste, no afecta al modelo
}

@Entity
@DiscriminatorColumn("C")
class Cerveza extends Bebida {
    @Enumerated
    variedad
}

@Entity
@DiscriminatorColumn("T")
class Trago extends Bebida {
}

@Entity
@DiscriminatorColumn("A")
class Analcohólica extends Bebida {
}

B)
1. Este problema se da ya que la operacion es muy costosa, debido a la cantidad de JOINS que se 
hacen para realizar el ranking, a partir de la Bebida hay que Joinear contra los pedidos y
hacer un SUM de la cantidad agrupando por bebida, ademas de que se requiere ordenar todo. 
Mientras mas pedidos se carguen mas veces se va a ejecutar la consulta.

2. Una solucion que podria funcionar es tener la cantidad de veces que fue pedida una bebida
precalculada como un campo en Bebida, entonces a la hora de hacer el ranking solamente tengo 
que consultar a la entidad Bebida haciendo un Order By por ese campo. Y para poder mantener
la consistencia con los nuevos pedidos que se vayan ingresando podria haber un trigger 
en Pedido que cada vez que se ingrese uno se actualiza el campo cantidad en la bebida
correspondiente.

C)
1. GET /locales/
Usuario obtenido a traves de la session

2. y 3. Como los query params se usan para filtrar a los recursos, en este caso los recursos
son los locales, voy a usarlos.
 
    GET /locales
    - query params (tipoBebida, palabraClave)
    - ejemplo:
        /locales?tipoBebida=trago&palabraClave=mojito
Usuario obtenido a traves de la session

4. 
a) GET /locales/:id

b) PATCH /usuario/:id
Ese boton tira esta request, la cual va a modificar parcialmente al usuario, cambiandole
el tipo de suscripcion nomas 

por limitacion de formulario: 
POST /usuario/:id
