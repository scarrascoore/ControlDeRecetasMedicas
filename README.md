# 💊 ControlDeMedicinas

Sistema web para gestionar medicamentos, horarios, tomas diarias y seguimiento del tratamiento.

## 📌 Descripción

**ControlDeMedicinas** es una aplicación web desarrollada para ayudar a organizar y controlar la administración de medicamentos de forma práctica, segura y ordenada.  
Permite registrar medicinas, definir horarios, visualizar las dosis del día y llevar un seguimiento del cumplimiento del tratamiento.

Este proyecto nace con el objetivo de reducir olvidos, mejorar la organización y brindar una mejor experiencia de control tanto a nivel personal como en entornos de supervisión.

---

## 🎯 Objetivo

Desarrollar una solución web que permita gestionar tratamientos médicos de forma centralizada, facilitando el registro de medicamentos, el control diario de tomas y el seguimiento histórico del cumplimiento.

---

## 🚀 Funcionalidades principales

- Registro de medicamentos
- Programación de horarios de toma
- Visualización de medicamentos del día
- Marcado de dosis como tomadas
- Historial de tomas realizadas
- Gestión de usuarios
- Autenticación segura
- Interfaz clara y organizada

---

## <img width="40" height="40" alt="folder" src="https://github.com/user-attachments/assets/d43f82a6-6e9b-45ed-abd7-d399f713db1a" /> Tecnologías utilizadas
 
### Frontend
- React
- TypeScript
- Vite
- React Router
- Axios

### Backend
- Node.js
- Express
- TypeScript
- JWT
- BCrypt

### Base de datos
- PostgreSQL

---
## 📂 Estructura del proyecto

```bash
ControlDeMedicinas/
├── backend/
├── frontend/
├── database/
├── docs/
└── README.md
```

## 🔐 Módulos del sistema

### 1. Autenticación y usuarios
- Inicio de sesión
- Protección de rutas
- Manejo seguro de credenciales

### 2. Gestión de medicamentos
- Crear medicamentos
- Editar medicamentos
- Eliminar medicamentos
- Configurar dosis, frecuencia y duración

### 3. Control diario
- Mostrar medicamentos programados para hoy
- Confirmar tomas realizadas
- Ver estado de cumplimiento diario

### 4. Historial y seguimiento
- Consultar tomas anteriores
- Revisar adherencia al tratamiento

## ⚙️ Instalación y ejecución

### Requisitos previos

- Node.js
- PostgreSQL
- Git

### Clonar el repositorio

```bash
git clone https://github.com/scarrascoore/ControlDeMedicinas.git
cd ControlDeMedicinas
```

### Backend

```bash
cd backend
npm install
npm run dev
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

## 📧 Configuración de correo (Google Gmail + JWT)

> Importante: en este proyecto, **JWT** se utiliza para la autenticación de usuarios dentro de la aplicación, mientras que la integración con **Gmail** debe configurarse mediante **OAuth 2.0 de Google**.

### ¿Qué se usa para cada cosa?

- **JWT**: para iniciar sesión en la aplicación, proteger rutas y validar sesiones.
- **Google OAuth 2.0**: para autorizar el envío y lectura de correos desde una cuenta Gmail.

---

## 🔑 Configuración de Google para Gmail API

Para integrar el envío o lectura de correos con Gmail, se requiere configurar credenciales en Google Cloud.

### Pasos generales

1. Ingresar a **Google Cloud Console**.
2. Crear un proyecto nuevo o usar uno existente.
3. Habilitar la **Gmail API**.
4. Configurar la pantalla de consentimiento OAuth.
5. Crear credenciales de tipo **OAuth Client ID**.
6. Registrar las **Redirect URIs** permitidas.

---

## 🧠 Diferencia clave

- **Google OAuth 2.0** → acceso autorizado a Gmail
- **JWT de la app** → autenticación interna de usuarios del sistema

No se recomienda confundir ambos mecanismos, porque cumplen funciones distintas.

