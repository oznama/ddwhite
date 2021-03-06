CREATE USER 'ddwhite'@'localhost' IDENTIFIED BY 'dDw4173MySql';
GRANT ALL PRIVILEGES ON ddwhite.* TO 'ddwhite'@'localhost';

truncate usuarios;

### dont work ### insert into usuarios (username, password, nombre_completo) values ('admin', aes_encrypt('myAdminPassword@',unhex('ddwhite')), 'Administrador');
#insert into usuarios (username, password, nombre_completo) values ('admin', aes_encrypt('myAdminPassword@',unhex(sha2('ddwhite',512))), 'Administrador');
#insert into usuarios (username, password, nombre_completo) values ('dummy', aes_encrypt('myDummyPassword@',unhex(sha2('ddwhite',512))), 'Usuario de prueba');
#select id, username, password, aes_decrypt(password,unhex(sha2('ddwhite',512))) as password_decrypt, nombre_completo from usuarios;

-- Roles
insert into roles(nombre, descripcion) values('root', 'Role super usuario sudo root');
insert into roles(nombre, descripcion) values('editor', 'Role con privilegios crear y actualizar');
insert into roles(nombre, descripcion) values('saler', 'Role exclusivo para realizar ventas');
insert into roles(nombre, descripcion) values('reporter', 'Role exclusivo para realizar reportes');

-- Privilegios
insert into privilegios(nombre, clave, descripcion) values('Registrar', 'INSRT', 'Privilegio para poder crear registros');
insert into privilegios(nombre, clave, descripcion) values('Modificar', 'UPDTE', 'Privilegio para poder modificar registros');
insert into privilegios(nombre, clave, descripcion) values('Borrar', 'DELTE', 'Privilegio para poder modificar registros');
insert into privilegios(nombre, clave, descripcion) values('Comprar', 'PURCH', 'Privilegio para poder realizar compras');
insert into privilegios(nombre, clave, descripcion) values('Ventas', 'SALE', 'Privilegio para poder realizar ventas');
insert into privilegios(nombre, clave, descripcion) values('Reportes', 'REPRT', 'Privilegio para poder generar reportes');

-- Roles privilegios
insert into roles_privilegios(id_role, id_privilegio) values(1, 1);
insert into roles_privilegios(id_role, id_privilegio) values(1, 2);
insert into roles_privilegios(id_role, id_privilegio) values(1, 3);
insert into roles_privilegios(id_role, id_privilegio) values(1, 4);
insert into roles_privilegios(id_role, id_privilegio) values(1, 5);
insert into roles_privilegios(id_role, id_privilegio) values(1, 6);

insert into roles_privilegios(id_role, id_privilegio) values(2, 1);
insert into roles_privilegios(id_role, id_privilegio) values(2, 2);
insert into roles_privilegios(id_role, id_privilegio) values(2, 4);
insert into roles_privilegios(id_role, id_privilegio) values(2, 5);
insert into roles_privilegios(id_role, id_privilegio) values(2, 6);

insert into roles_privilegios(id_role, id_privilegio) values(3, 5);

insert into roles_privilegios(id_role, id_privilegio) values(4, 6);


insert into usuarios (username, password, nombre_completo, id_role) values ('root', 'myR0o7PaSsW0rd', 'Root user', 1);
update usuarios set username = 'root' where username = 'admin';
update usuarios set password = '12345678' where username = 'Chris';

select * from compras c where c.id_producto = 8;
/*
 * 13	2	11	8	2	550.0000	28	2020-09-16 17:30:18.0	20
 * 19	2	11	8	10	60.0000	2	2020-09-17 10:51:17.0	
 */

update compras set cantidad = 2 where id = 13;
update compras set cantidad = 10 where id = 19;


truncate table catalogos;

insert into catalogos(nombre, descripcion) values('CONST', 'CONSTANTES');
insert into catalogos(nombre, descripcion, catalogo_padre) values ('MONTO_MIN_CAJA', '200', (select id from (select id from catalogos where nombre = 'CONST') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('MONTO_MAX_CAJA', '500', (select id from (select id from catalogos where nombre = 'CONST') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('RETIRO_MULTIPLO', '500', (select id from (select id from catalogos where nombre = 'CONST') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('IVA', '16', (select id from (select id from catalogos where nombre = 'CONST') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('DESCUENTO_ACTIVO', 'SI', (select id from (select id from catalogos where nombre = 'CONST') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('MYSQL_PATH', 'C:\\Program Files\\MySQL\\MySQL Server 5.7', (select id from (select id from catalogos where nombre = 'CONST') as id_catalog_parent));

insert into catalogos(nombre, descripcion) values('PINPAD', 'COMISIONES PINPAD');
insert into catalogos(nombre, descripcion, catalogo_padre) values ('UNA SOLA EXIBICION', '3.6%+IVA', (select id from (select id from catalogos where nombre = 'PINPAD') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('3 MESES', '3.6%+4.5%+IVA', (select id from (select id from catalogos where nombre = 'PINPAD') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('6 MESES', '3.6%+7.5%+IVA', (select id from (select id from catalogos where nombre = 'PINPAD') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('9 MESES', '3.6%+9.9%+IVA', (select id from (select id from catalogos where nombre = 'PINPAD') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('12 MESES', '3.6%+11.95%+IVA', (select id from (select id from catalogos where nombre = 'PINPAD') as id_catalog_parent));

insert into catalogos(nombre, descripcion) values('DESCUENTO', 'DESCUENTOS');
insert into catalogos(nombre, descripcion, catalogo_padre) values ('10%', '10', (select id from (select id from catalogos where nombre = 'DESCUENTO') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('20%', '20', (select id from (select id from catalogos where nombre = 'DESCUENTO') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('30%', '30', (select id from (select id from catalogos where nombre = 'DESCUENTO') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('40%', '40', (select id from (select id from catalogos where nombre = 'DESCUENTO') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('50%', '50', (select id from (select id from catalogos where nombre = 'DESCUENTO') as id_catalog_parent));

insert into catalogos(nombre, descripcion) values('DENOMINACION', 'DENOMINACIONES DE EFECTIVO');
insert into catalogos(nombre, descripcion, catalogo_padre) values ('0.10', '10 CENTAVOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('0.20', '20 CENTAVOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('0.50', '50 CENTAVOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('1', '1 PESO', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('2', '2 PESOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('5', '5 PESOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('10', '10 PESOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('20', '20 PESOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('50', '50 PESOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('100', '100 PESOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('200', '200 PESOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('500', '500 PESOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));

-- DATOS COMPAnIA
insert into catalogos(nombre, descripcion) values('COMPANY', 'DATOS DE LA COMPANIA');
-- UNIDADES DE MEDIDA PARA PRODUCTOS
insert into catalogos(nombre, descripcion) values('UNIDADES', 'UNIDADES DE MEDIDA DE LOS PRODUCTOS');
-- CATALOGO GRUPOS DE PRODUCTOS
insert into catalogos(nombre, descripcion) values('GRUPOPROD', 'GRUPOS DE PRODUCTOS');
-- METODOS DE PAGO
insert into catalogos(nombre, descripcion) values('METODPAG', 'METODOS DE PAGO');
insert into catalogos(nombre, catalogo_padre) values ('EFECTIVO', (select id from (select id from catalogos where nombre = 'METODPAG') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('TARJETA DEBITO', 'COMISION', (select id from (select id from catalogos where nombre = 'METODPAG') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('TARJETA CREDITO', 'COMISION', (select id from (select id from catalogos where nombre = 'METODPAG') as id_catalog_parent));


insert into catalogos(nombre, descripcion , catalogo_padre) values ('NOMBRE', 'Deposito Dental Santa Agata', (select id from (select id from catalogos where nombre = 'COMPANY') as id_catalog_parent));
insert into catalogos(nombre, descripcion , catalogo_padre) values ('DIRECCION', 'Rio la Venta #132, Lomas de Rio Medio 91809, Veracruz, Veracruz', (select id from (select id from catalogos where nombre = 'COMPANY') as id_catalog_parent));
insert into catalogos(nombre, descripcion , catalogo_padre) values ('TELEFONO', '2292863469', (select id from (select id from catalogos where nombre = 'COMPANY') as id_catalog_parent));
insert into catalogos(nombre, descripcion , catalogo_padre) values ('MESSAGE_TICKET', '¡GRACIAS POR SU COMPRA!', (select id from (select id from catalogos where nombre = 'COMPANY') as id_catalog_parent));
#insert into catalogos(nombre, descripcion , catalogo_padre) values ('PAGINA', 'www.ddsantaagata.com.mx', (select id from (select id from catalogos where nombre = 'COMPANY') as id_catalog_parent));
insert into catalogos(nombre, descripcion , catalogo_padre) values ('EMAIL', 'chrisgutili@gmail.com', (select id from (select id from catalogos where nombre = 'COMPANY') as id_catalog_parent));
insert into catalogos(nombre, descripcion , catalogo_padre) values ('NOMBRE_FISCAL', 'Christian Gutierrez Limon', (select id from (select id from catalogos where nombre = 'COMPANY') as id_catalog_parent));
insert into catalogos(nombre, descripcion , catalogo_padre) values ('RFC', 'GULC970210ER3', (select id from (select id from catalogos where nombre = 'COMPANY') as id_catalog_parent));

insert into catalogos(nombre, descripcion, catalogo_padre) values ('PZA', 'UNIDAD MINIMA UNITARIA', (select id from (select id from catalogos where nombre = 'UNIDADES') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('CAJA', 'CAJA CON PIEZAS', (select id from (select id from catalogos where nombre = 'UNIDADES') as id_catalog_parent));


insert into catalogos(nombre, catalogo_padre) values ('CONSUMIBLES', (select id from (select id from catalogos where nombre = 'GRUPOPROD') as id_catalog_parent));
insert into catalogos(nombre, catalogo_padre) values ('ACIDOS GRABADORES', (select id from (select id from catalogos where nombre = 'GRUPOPROD') as id_catalog_parent));
insert into catalogos(nombre, catalogo_padre) values ('ADESIVOS/BOND', (select id from (select id from catalogos where nombre = 'GRUPOPROD') as id_catalog_parent));

alter table retiro_detalle drop foreign key retiro_detalle_ibfk_1;
alter table retiro_detalle drop foreign key retiro_detalle_ibfk_2;
alter table retiros drop foreign key retiros_ibfk_1;
alter table sesion drop foreign key sesion_ibfk_1;
alter table venta_pago drop foreign key venta_pago_ibfk_1;
alter table venta_pago drop foreign key venta_pago_ibfk_2;
alter table venta_detalle drop foreign key venta_detalle_ibfk_1;
alter table venta_detalle drop foreign key venta_detalle_ibfk_2;
alter table venta_total drop foreign key venta_total_ibfk_1;
alter table venta_total drop foreign key venta_total_ibfk_2;
alter table compras_reasignadas drop foreign key compras_reasignadas_ibfk_1;
alter table compras_reasignadas drop foreign key compras_reasignadas_ibfk_2;
alter table compras drop foreign key compras_ibfk_1;
alter table compras drop foreign key compras_ibfk_2;
alter table compras drop foreign key compras_ibfk_3;
alter table compras drop foreign key compras_ibfk_4;
#alter table productos drop foreign key productos_ibfk_1;
truncate compras_reasignadas;
truncate compras;
truncate venta_pago;
truncate venta_detalle;
truncate venta_total;
truncate retiro_detalle;
truncate retiros;
truncate sesion;
#truncate productos;
#alter table proveedores add constraint foreign key (id_usuario) references usuarios (id);
#alter table productos add constraint foreign key (id_usuario) references usuarios (id);
#alter table gastos add constraint foreign key (id_usuario) references usuarios (id);
alter table compras_reasignadas add constraint foreign key (id_compra_origen) references compras (id);
alter table compras_reasignadas add constraint foreign key (id_compra_destino) references compras (id);
alter table compras add constraint foreign key (id_usuario) references usuarios (id);
alter table compras add constraint foreign key (id_proveedor) references proveedores (id);
alter table compras add constraint foreign key (id_producto) references productos (id);
alter table compras add constraint foreign key (unidad) references catalogos (id);
#alter table clientes add constraint foreign key (id_usuario) references usuarios (id);
alter table venta_total add constraint foreign key (id_usuario) references usuarios (id);
alter table venta_total add constraint foreign key (id_cliente) references clientes (id);
alter table venta_detalle add constraint foreign key (id_venta) references venta_total (id);
alter table venta_detalle add constraint foreign key (id_producto) references productos (id);
alter table venta_pago add constraint foreign key (id_venta) references venta_total (id);
alter table venta_pago add constraint foreign key (forma_pago) references catalogos (id);
alter table retiro_detalle add constraint foreign key (id_retiro) references retiros (id);
alter table retiro_detalle add constraint foreign key (denominacion) references catalogos (id);
alter table retiros add constraint foreign key (id_sesion) references sesion (id);
alter table sesion add constraint foreign key (id_usuario) references usuarios (id);



 #### ALTERS FOR PRECISIONS DECIMAL ########
alter table productos modify column porcentaje_ganancia decimal(5,2) not null;
alter table productos modify column costo decimal(10,4) default 0;
alter table compras modify column costo decimal(10,4) not null;
alter table venta_total modify column sub_total decimal(20,2) not null;
alter table venta_total modify column iva decimal(10,2) not null;
alter table venta_total modify column total decimal(20,2) not null;
alter table venta_detalle modify column precio decimal(10,2);
alter table venta_pago modify column monto decimal(20,2);
alter table usuarios add column id_role bigint;
update usuarios set id_role = 1 where id = 1;
update usuarios set id_role = 2 where id = 2;
update usuarios set id_role = 3 where id = 5;
alter table gastos add column deducible bit(1) not null;
alter table gastos add column folio_factura char(100);
update gastos set deducible = 1 where id in (1,4, 6,7);
update gastos set folio_factura = '34ru89-34r890-90wff-90sdf' where id = 1;
update gastos set folio_factura = 'sdojf0-90s8df-kjbv4-je479' where id = 4;
update gastos set folio_factura = 'bvc89p-fv97er-90sdf-skop5' where id = 7;
alter table venta_total add column folio_factura char(100);
update venta_total set folio_factura = 'vmerui54-090iker-fverio-dfdfiovc' where id = 3;
update venta_total set folio_factura = 'cei489-jmedu4ug-cvmed348-96uuyed' where id = 7;
update venta_total set folio_factura = 'slfu90794-sdjkfguyh895-0g90ejk34' where id = 13;
alter table compras add column num_piezas int;
alter table venta_detalle add column unidad bigint;
alter table venta_detalle add column num_piezas int;
alter table productos drop column costo;
alter table compras modify column cantidad decimal(10,4) not null;
alter table venta_detalle modify column cantidad decimal(10,4) not null;



################################################
################################################
## ALTERS NUEVA VERSION PRECIOSO 05/10/2020
################################################
alter table venta_pago add column folio_voucher char(99);
alter table venta_pago add column comision decimal(10,2);
insert into catalogos(nombre, descripcion) values('CONST', 'CONSTANTES');
insert into catalogos(nombre, descripcion, catalogo_padre) values ('MONTO_MIN_CAJA', '200', (select id from (select id from catalogos where nombre = 'CONST') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('IVA', '16', (select id from (select id from catalogos where nombre = 'CONST') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('DESCUENTO_ACTIVO', 'SI', (select id from (select id from catalogos where nombre = 'CONST') as id_catalog_parent));

insert into catalogos(nombre, descripcion) values('PINPAD', 'COMISIONES PINPAD');
insert into catalogos(nombre, descripcion, catalogo_padre) values ('UNA SOLA EXIBICION', '3.6%+IVA', (select id from (select id from catalogos where nombre = 'PINPAD') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('3 MESES', '3.6%+4.5%+IVA', (select id from (select id from catalogos where nombre = 'PINPAD') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('6 MESES', '3.6%+7.5%+IVA', (select id from (select id from catalogos where nombre = 'PINPAD') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('9 MESES', '3.6%+9.9%+IVA', (select id from (select id from catalogos where nombre = 'PINPAD') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('12 MESES', '3.6%+11.95%+IVA', (select id from (select id from catalogos where nombre = 'PINPAD') as id_catalog_parent));

## ALTERS 08/10/2020
alter table venta_total add column descuento decimal(10,2);
alter table sesion add column retiro datetime default current_timestamp;
insert into catalogos(nombre, descripcion, catalogo_padre) values ('MYSQL_PATH', 'C:\\Program Files\\MySQL\\MySQL Server 5.7', (select id from (select id from catalogos where nombre = 'CONST') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('MONTO_MAX_CAJA', '500', (select id from (select id from catalogos where nombre = 'CONST') as id_catalog_parent));
insert into catalogos(nombre, descripcion) values('DESCUENTO', 'DESCUENTOS');
insert into catalogos(nombre, descripcion, catalogo_padre) values ('10%', '10', (select id from (select id from catalogos where nombre = 'DESCUENTO') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('20%', '20', (select id from (select id from catalogos where nombre = 'DESCUENTO') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('30%', '30', (select id from (select id from catalogos where nombre = 'DESCUENTO') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('40%', '40', (select id from (select id from catalogos where nombre = 'DESCUENTO') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('50%', '50', (select id from (select id from catalogos where nombre = 'DESCUENTO') as id_catalog_parent));
insert into catalogos(nombre, descripcion) values('DENOMINACION', 'DENOMINACIONES DE EFECTIVO');
insert into catalogos(nombre, descripcion, catalogo_padre) values ('0.10', '10 CENTAVOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('0.50', '50 CENTAVOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('1', '1 PESO', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('2', '2 PESOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('5', '5 PESOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('10', '10 PESOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('20', '20 PESOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('50', '50 PESOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('100', '100 PESOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('200', '200 PESOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('500', '500 PESOS', (select id from (select id from catalogos where nombre = 'DENOMINACION') as id_catalog_parent));
################################################

## ALTERS 20/10/2020
## CREATES DE RETIROS
alter table sesion drop column retiro;
################################################

## ALTERS 20/10/2020
## CREATES DE RETIROS
alter table clientes modify column email char(80);
rename table compras_reasingadas to compras_reasignadas;
alter table sesion add column monto_final decimal(10,4);
update catalogos set descripcion = 'COMISION' where id in (6,7);
################################################
alter table gastos modify column fecha_registro datetime not null;
################################################
## ALTERS 05/11/2020
insert into catalogos(nombre, descripcion, catalogo_padre) values ('RETIRO_MULTIPLO', '500', (select id from (select id from catalogos where nombre = 'CONST') as id_catalog_parent));
################################################
################################################
################################################
################################################
################################################
