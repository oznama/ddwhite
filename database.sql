create database ddwhite;

drop table if exists usuarios;
create table if not exists usuarios(
	id bigint not null auto_increment,
	username char(10) not null,
	password char(32) not null,
	nombre_completo char(100) not null,
	primary key (id)
);

drop table if exists proveedores;
create table if not exists proveedores(
	id bigint not null auto_increment,
	razon_social char(150) not null,
	direccion char(80),
	telefono char(20),
	pagina_web char(100),
	contacto char(100) not null,
	id_usuario bigint not null,
	primary key (id)
);
alter table proveedores add constraint foreign key (id_usuario) references usuarios (id);
