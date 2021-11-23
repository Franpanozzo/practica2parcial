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
en el algoritmo de recomendar


B)
Primero una pantalla donde puedas elegir tu categoria preferida
- Ingresar categoria y coccion preferida: GET /preferencias   Devuelve todas las categorias, cocciones  para elegir 
Request que se manda: POST /pedido   Un nuevo pedido
                        body 
                            { categorias
                            cocciones }

Se guarda y se redirecciona a elegir el tipo de pedido

GET /tiposPedidos

metodo mas correcto ya que se modifica parcialmente la preferencia
request: PATCH /pedido/:id  //o UPDATE tambien podria ser

Por limitacion de formulario: POST /pedido/:id

Redirecciona a mostrar el set recomendado

GET /pedido/:id/setRecomendado
