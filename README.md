# **ShowAnimalDADM / appnimal v2.0**
***
## **app**nimal :: *una aplicación con sistema de autentificación que muestra un catálogo de animales e información adicional de cada uno de ellos recabando información de un API*
***

**Objetivo**: 

Practicar el manejo de datos en línea en la plataforma Android con Kotlin mediante la utilización de bibliotecas, considerando su formato y su correspondiente procesamiento en una aplicación. Adicionalmente, aprenderá a crear sus propios servicios de prueba con algunas herramientas Web disponibles para su consumo.

**Objetivo v2.0**:

Practicar conceptos de seguridad, autenticación y la herramienta de Mapas de Google en la plataforma Android con Kotlin mediante la utilización de bibliotecas y componentes que permiten aprovechar más a fondo las capacidades técnicas de los dispositivos móviles, extendiendo las características de una aplicación y permitiendo una mayor cantidad de funcionalidades para los usuarios.

![Mokup appnimal movil](https://idel.com.mx/dadm/mokup.jpg)  
![Mokup appnimal movil](https://idel.com.mx/dadm/mock2.jpg)  

**Características**:
* Splash screen al entrar a la aplicación en donde aparece el logo, la versión y el creador de la aplicación por alrededor de 3 segundos. `OK`
* Creación de un API mediante la herramienta Web apiary ([apiary.io](https://apiary.io/)) con sus endpoints para consumir y mostrar un catálogo de elementos y sus detalles. `OK`
* El listado principal contiene una imagen por cada elemento usando url’s. `OK`
* El tipo de elementos (libre elección), con detalles y una cantidad mínima de detalles o atributos de 6. `OK`
* La interfaz adecuada al tipo de aplicación y su temática. `OK`
* En la pantalla principal muestra el listado de elementos y al seleccionar alguno de ellos se visualizan sus detalles. `OK`

**Características NEW**:

* Ingreso con autenticación mediante un correo electrónico y contraseña. `OK`
* La autenticación mediante la herramienta FirebaseAuth de Firebase. `OK`
* Funcionalidad para restablecer la contraseña en caso de olvido de la misma. `OK`
* Detalles agregados al catálogo de elementos del API - Apiary para recibir información de una ubicación. `OK`
* Integración de la ubicación al tipo de elementos en su catálogo. `OK`
* La veracidad de las ubicaciones no es relevante, en tanto sean en general diferentes para cada elemento. `OK`
* Implementar (dentro de la pantalla con los detalles) la funcionalidad para mostrar la ubicación ligada al item seleccionado en un mapa usando Google Maps, posicionando sobre ella un marcador personalizado. `OK`

***
## **API de datos mock**
***
Se creo API de datos mock en la plataforma [apiary.io](https://apiary.io/) 

- ### URL con documentación: [idel.docs.apiary.io](https://idel.docs.apiary.io/)
- ### URL con base de consulta: [private-19eea8-idel.apiary-mock.com](https://private-19eea8-idel.apiary-mock.com/)

#### Endpoints:

### [/user?id={id_user}](https://private-19eea8-idel.apiary-mock.com/user?id=1)

`Optiene información base de usuario [id, nombre , url de avatar]; (id_user disponibles -> [1,2,3,4,5])`

Respuesta - estructura json esperada  
~~~
    {
        "id": "1",
        "nombre": "Idel",
        "imagen": "https://idel.com.mx/imagenes/info/foto.jpg"
        "correo": "correo@correo.com"
    }
~~~
_Nota: en la v2.0, se agregaron 3 id's adicioneles, el login obtiene de forma random uno de ellos para simular la recuperacion de otros datos del perfil como lo son NOMBRE y AVATAR de un backend, asi mismo refresca los datos durante ejecución de ser necesario._


### [/animalesall](https://private-19eea8-idel.apiary-mock.com/animalesall)
`Optiene areglo de animales con información basica [id, nombre , url de imagen]`

Respuesta - estructura json esperada
~~~
    [
        {
            "id": "1",
            "nombre": "Oso",
            "imagen": "https://idel.com.mx/dadm/oso.gif"
        }, {
            "id": "2",
            "nombre": "Pato",
            "imagen": "https://idel.com.mx/dadm/pato.gif"
        }
    ]
~~~

### [/animal?id={id_animal}](https://private-19eea8-idel.apiary-mock.com/animal?id=1)
`Optiene información detalle de animal [id, nombre , url de imagen, dueño, peso, genero, si esta enfermo y descripción]; ID's disponibles - [1,2,3,4,5,6,7,8,9,10]`

Respuesta - estructura json esperada
~~~
    {
        "id": "1",
        "nombre": "Oso",
        "imagen": "https://idel.com.mx/dadm/oso.gif",
        "dueno": "Manuel Flores",
        "peso": 520.25,
        "genero": true,
        "enfermo": false,
        "latitude":49.935997,
        "longitude":40.553506,
        "descripcion": "Descripción de animal"
    }
~~~
_Nota: en la v2.0, se agregaron los atributos adicionales latitude y latitude usados en la aplicación para señalar ubicación de cada item_
***
## **Créditos**
***
>Elaborado por:  
**[Idelfonso De la Cruz Hernández](https://idel.com.mx/)**  
  
>Desarrollo de Aplicaciones para Dispositivos Móviles  
9ª emisión  
  
>Módulo: Desarrollo avanzado de aplicaciones para Android  
Enero de 2023 - Version 2.0.