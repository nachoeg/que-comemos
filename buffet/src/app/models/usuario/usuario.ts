export class Usuario {
    
        "id": number;
        "dni": number;
        "nombre": string;
        "apellido": string;
        "email": string;
        "clave": string;
        "foto": string;
        "rol": number;

        constructor(id: number, dni: number, nombre: string, apellido: string, email: string, clave: string, foto: string, rol: number){
            this.id = id;
            this.dni = dni;
            this.nombre = nombre;
            this.apellido = apellido;
            this.email = email;
            this.clave = clave;
            this.foto = foto;
            this.rol = rol;
        }
      }
