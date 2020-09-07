create database ddwhite default character SET utf8;

drop table if exists venta_pago;
drop table if exists venta_detalle;
drop table if exists venta_total;
drop table if exists clientes;
drop table if exists gastos;
drop table if exists compras;
drop table if exists productos;
drop table if exists proveedores;
drop table if exists usuarios;
drop table if exists privilegios;
drop table if exists roles;
drop table if exists catalogos;

create table if not exists catalogos(
	id bigint not null auto_increment,
	nombre char(20) not null,
	descripcion char(100),
	catalogo_padre bigint,
	primary key(id)
);

create table if not exists roles(
	id bigint not null auto_increment,
	nombre char(50) not null,
	descripcion char(100),
	primary key(id)
);

create table if not exists privilegios(
	id bigint not null auto_increment,
	nombre char(50) not null,
	clave char(5) not null,
	descripcion char(100),
	primary key(id)
);

create table if not exists roles_privilegios(
	id bigint not null auto_increment,
	id_role bigint not null,
	id_privilegio bigint not null,
	primary key(id)
);
alter table roles_privilegios add constraint foreign key (id_role) references roles (id);
alter table roles_privilegios add constraint foreign key (id_privilegio) references privilegios (id);

create table if not exists usuarios(
	id bigint not null auto_increment,
	username char(10) not null,
	password char(32) not null,
	nombre_completo char(100) not null,
	fecha_registro datetime default current_timestamp,
	id_role bigint not null,
	primary key (id)
);
alter table usuarios add constraint foreign key (id_role) references roles (id);

create table if not exists proveedores(
	id bigint not null auto_increment,
	razon_social char(150) not null,
	direccion char(80),
	telefono char(20),
	pagina_web char(100),
	contacto char(100) not null,
	id_usuario bigint not null,
	fecha_registro datetime default current_timestamp,
	primary key (id)
);
alter table proveedores add constraint foreign key (id_usuario) references usuarios (id);

create table if not exists productos(
	id bigint not null auto_increment,
	nombre_largo char(80) not null,
	nombre_corto char(10) not null,
	sku char(15) unique not null,
	descripcion char(255),
	porcentaje_ganancia decimal(5,2) not null,
	costo decimal(10,2) default 0,
	grupo bigint not null,
	fecha_registro datetime default current_timestamp,
	id_usuario bigint not null,
	primary key(id)
);
alter table productos add constraint foreign key (id_usuario) references usuarios (id);

create table if not exists gastos(
	id bigint not null auto_increment,
	id_usuario bigint not null,
	descripcion char(255) not null,
	monto decimal(20,2) not null,
	fecha_registro datetime default current_timestamp,
	deducible bit(1) not null,
	folio_factura char(100),
	primary key(id)
);
alter table gastos add constraint foreign key (id_usuario) references usuarios (id);

create table if not exists compras(
	id bigint not null auto_increment,
	id_usuario bigint not null,
	id_proveedor bigint not null,
	id_producto bigint not null,
	cantidad int not null,
	costo decimal(10,2) not null,
	unidad bigint,
	fecha_registro datetime default current_timestamp,
	primary key(id)
);
alter table compras add constraint foreign key (id_usuario) references usuarios (id);
alter table compras add constraint foreign key (id_proveedor) references proveedores (id);
alter table compras add constraint foreign key (id_producto) references productos (id);
alter table compras add constraint foreign key (unidad) references catalogos (id);

create table if not exists clientes(
	id bigint not null auto_increment,
	nombre char(100) not null,
	ap_paterno char(50) not null,
	ap_materno char(50) not null,
	domicilio_particular char(80) not null,
	telefono char(20) not null,
	email char(30),
	rfc char(20),
	domicilio_fiscal char(80),
	telefono_fiscal char(20),
	id_usuario bigint not null,
	fecha_registro datetime default current_timestamp,
	primary key(id)
);
alter table clientes add constraint foreign key (id_usuario) references usuarios (id);

create table if not exists venta_total(
	id bigint not null auto_increment,
	id_usuario bigint not null,
	id_cliente bigint,
	folio_factura char(100),
	sub_total decimal(20,2) not null,
	iva decimal(10,2) not null,
	total decimal(20,2) not null,
	cambio decimal(20,2) not null,
	fecha_registro datetime default current_timestamp,
	primary key(id)
);
alter table venta_total add constraint foreign key (id_usuario) references usuarios (id);
alter table venta_total add constraint foreign key (id_cliente) references clientes (id);

create table if not exists venta_detalle(
	id bigint not null auto_increment,
	id_venta bigint not null,
	id_producto bigint not null,
	cantidad int not null,
	precio decimal(10,2),
	primary key(id)
);
alter table venta_detalle add constraint foreign key (id_venta) references venta_total (id);
alter table venta_detalle add constraint foreign key (id_producto) references productos (id);

create table if not exists venta_pago(
	id bigint not null auto_increment,
	id_venta bigint not null,
	forma_pago bigint not null,
	monto decimal(20,2),
	primary key(id)
);
alter table venta_pago add constraint foreign key (id_venta) references venta_total (id);
alter table venta_pago add constraint foreign key (forma_pago) references catalogos (id);