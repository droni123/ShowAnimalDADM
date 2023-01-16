# **ShowAnimalDADM / appnimal**
***
## **app**nimal :: *una aplicación que muestra un catálogo de animales e información adicional de cada uno de ellos recabando información de un API*
***

**Objetivo**: 

Practicar el manejo de datos en línea en la plataforma Android con Kotlin mediante la utilización de bibliotecas, considerando su formato y su correspondiente procesamiento en una aplicación. Adicionalmente, aprenderá a crear sus propios servicios de prueba con algunas herramientas Web disponibles para su consumo.

![Mokup appnimal movil](https://idel.com.mx/dadm/mokup.jpg)  

**Características**:
* Splash screen al entrar a la aplicación en donde aparece el logo, la versión y el creador de la aplicación por alrededor de 3 segundos. `OK`
* Creación de un API mediante la herramienta Web apiary ([apiary.io](https://apiary.io/)) con sus endpoints para consumir y mostrar un catálogo de elementos y sus detalles. `OK`
* El listado principal contiene una imagen por cada elemento usando url’s. `OK`
* El tipo de elementos (libre elección), con detalles y una cantidad mínima de detalles o atributos de 6. `OK`
* La interfaz adecuada al tipo de aplicación y su temática. `OK`
* En la pantalla principal muestra el listado de elementos y al seleccionar alguno de ellos se visualizan sus detalles. `OK`

***
## **API de datos mock**
***
Se creo API de datos mock en la plataforma [apiary.io](https://apiary.io/) 

- ### URL con documentación: [idel.docs.apiary.io](https://idel.docs.apiary.io/)
- ### URL con base de consulta: [private-19eea8-idel.apiary-mock.com](https://private-19eea8-idel.apiary-mock.com/)

#### Endpoints:

### [/user?id={id_user}](https://private-19eea8-idel.apiary-mock.com/user?id=1)

`Optiene información base de usuario [id, nombre , url de avatar]; (id_user disponibles -> [1,2])`

Respuesta - estructura json esperada  
~~~
    {
        "id": "1",
        "nombre": "Idel",
        "imagen": "https://idel.com.mx/imagenes/info/foto.jpg"
    }
~~~

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
        "descripcion": "Descripción de animal"
    }
~~~

***
## **Créditos**
***
>Elaborado por:  
**[Idelfonso De la Cruz Hernández](https://idel.com.mx/)**  
  
>Desarrollo de Aplicaciones para Dispositivos Móviles  
9ª emisión  
  
>Módulo: Desarrollo avanzado de aplicaciones para Android  
Enero de 2023 - Version 1.0.