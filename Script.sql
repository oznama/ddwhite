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
insert into catalogos(nombre, catalogo_padre) values ('TARJETA DEBITO', (select id from (select id from catalogos where nombre = 'METODPAG') as id_catalog_parent));
insert into catalogos(nombre, catalogo_padre) values ('TARJETA CREDITO', (select id from (select id from catalogos where nombre = 'METODPAG') as id_catalog_parent));


insert into catalogos(nombre, descripcion , catalogo_padre) values ('NOMBRE', 'Deposito Dental Santa Agata', (select id from (select id from catalogos where nombre = 'COMPANY') as id_catalog_parent));
insert into catalogos(nombre, descripcion , catalogo_padre) values ('DIRECCION', 'Rio la Venta #132, Lomas de Rio Medio 91809, Veracruz, Veracruz', (select id from (select id from catalogos where nombre = 'COMPANY') as id_catalog_parent));
insert into catalogos(nombre, descripcion , catalogo_padre) values ('TELEFONO', '2292863469', (select id from (select id from catalogos where nombre = 'COMPANY') as id_catalog_parent));
insert into catalogos(nombre, descripcion , catalogo_padre) values ('MESSAGE_TICKET', '¡GRACIAS POR SU COMPRA!', (select id from (select id from catalogos where nombre = 'COMPANY') as id_catalog_parent));
#insert into catalogos(nombre, descripcion , catalogo_padre) values ('PAGINA', 'www.ddsantaagata.com.mx', (select id from (select id from catalogos where nombre = 'COMPANY') as id_catalog_parent));
insert into catalogos(nombre, descripcion , catalogo_padre) values ('EMAIL', 'chrisgutili@gmail.com', (select id from (select id from catalogos where nombre = 'COMPANY') as id_catalog_parent));
insert into catalogos(nombre, descripcion , catalogo_padre) values ('NOMBRE_FISCAL', 'Christian Gutierrez Limon', (select id from (select id from catalogos where nombre = 'COMPANY') as id_catalog_parent));
insert into catalogos(nombre, descripcion , catalogo_padre) values ('RFC', 'GULC970210ER3', (select id from (select id from catalogos where nombre = 'COMPANY') as id_catalog_parent));

insert into catalogos(nombre, descripcion, catalogo_padre) values ('PIEZA', 'UNIDAD MINIMA UNITARIA', (select id from (select id from catalogos where nombre = 'UNIDADES') as id_catalog_parent));
insert into catalogos(nombre, descripcion, catalogo_padre) values ('CAJA', 'CAJA CON PIEZAS', (select id from (select id from catalogos where nombre = 'UNIDADES') as id_catalog_parent));


insert into catalogos(nombre, catalogo_padre) values ('CONSUMIBLES', (select id from (select id from catalogos where nombre = 'GRUPOPROD') as id_catalog_parent));
insert into catalogos(nombre, catalogo_padre) values ('ACIDOS GRABADORES', (select id from (select id from catalogos where nombre = 'GRUPOPROD') as id_catalog_parent));
insert into catalogos(nombre, catalogo_padre) values ('ADESIVOS/BOND', (select id from (select id from catalogos where nombre = 'GRUPOPROD') as id_catalog_parent));


alter table venta_pago drop foreign key venta_pago_ibfk_1;
alter table venta_pago drop foreign key venta_pago_ibfk_2;
alter table venta_detalle drop foreign key venta_detalle_ibfk_1;
alter table venta_detalle drop foreign key venta_detalle_ibfk_2;
alter table venta_total drop foreign key venta_total_ibfk_1;
alter table venta_total drop foreign key venta_total_ibfk_2;
alter table compras drop foreign key compras_ibfk_1;
alter table compras drop foreign key compras_ibfk_2;
alter table compras drop foreign key compras_ibfk_3;
alter table compras drop foreign key compras_ibfk_4;
alter table productos drop foreign key productos_ibfk_1;
truncate compras;
truncate venta_pago ;
truncate venta_detalle ;
truncate venta_total ;
truncate productos;
alter table venta_detalle add constraint foreign key (id_venta) references venta_total (id);
alter table venta_pago add constraint foreign key (id_venta) references venta_total (id);
alter table proveedores add constraint foreign key (id_usuario) references usuarios (id);
alter table productos add constraint foreign key (id_usuario) references usuarios (id);
alter table gastos add constraint foreign key (id_usuario) references usuarios (id);
alter table compras add constraint foreign key (id_usuario) references usuarios (id);
alter table compras add constraint foreign key (id_proveedor) references proveedores (id);
alter table compras add constraint foreign key (id_producto) references productos (id);
alter table compras add constraint foreign key (unidad) references catalogos (id);
alter table clientes add constraint foreign key (id_usuario) references usuarios (id);
alter table venta_total add constraint foreign key (id_usuario) references usuarios (id);
alter table venta_total add constraint foreign key (id_cliente) references clientes (id);
alter table venta_detalle add constraint foreign key (id_venta) references venta_total (id);
alter table venta_detalle add constraint foreign key (id_producto) references productos (id);
alter table venta_pago add constraint foreign key (id_venta) references venta_total (id);
alter table venta_pago add constraint foreign key (forma_pago) references catalogos (id);



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

select id, nombre_largo , porcentaje_ganancia from productos;
update productos set porcentaje_ganancia=55 where id between 8 and 107;


######### Reportes #############

# Compras en un rango
select u.nombre_completo as usuario, 
	pr.razon_social as proveedor, 
	p.nombre_largo as producto, cg.nombre as grupo, 
	c.cantidad, c.costo, 
	(c.cantidad * c.costo) as total, round(((c.cantidad * c.costo)/1.16),2) as subtotal, round( ((c.cantidad * c.costo) - ((c.cantidad * c.costo)/1.16)),2 ) as iva, 
	c.fecha_registro 
from compras c
inner join productos p on p.id = c.id_producto
inner join proveedores pr on pr.id = c.id_proveedor
inner join usuarios u on u.id = c.id_usuario
inner join catalogos cg on cg.id = p.grupo
where c.fecha_registro  between '2020-08-01 00:00:00' and '2020-08-31 11:59:59';

-- Total
select sum(c.cantidad) productos_comprados, sum(c.costo) as costo_total, 
	sum(c.cantidad * c.costo) as total, sum(round(((c.cantidad * c.costo)/1.16),2)) as subtotal, sum(round( ((c.cantidad * c.costo) - ((c.cantidad * c.costo)/1.16)),2 )) as iva, 
	c.fecha_registro 
from compras c
where c.fecha_registro  between '2020-08-01 00:00:00' and '2020-08-31 11:59:59';

	## Gastos separados
	select u.nombre_completo as usuario, '', g.descripcion as gasto, 'Gastos' as grupo, 1, g.monto,
		g.monto, round((g.monto/1.16),2) as subtotal, round( (g.monto - (g.monto/1.16)),2 ) as iva, 
		g.fecha_registro 
	from gastos g
	inner join usuarios u on u.id = g.id_usuario ;

# Ventas en un rango
select vt.id as id_venta, u.nombre_completo as usuario,
	upper(concat(c.rfc, ' ', c.nombre, ' ', c.ap_paterno, ' ', c.ap_materno )) as cliente,
	p.nombre_largo as producto,
	vd.cantidad, vd.precio,
	(vd.cantidad * vd.precio) as total, round(((vd.cantidad * vd.precio)/1.16),2) as subtotal, round( ((vd.cantidad * vd.precio) - ((vd.cantidad * vd.precio)/1.16)),2 ) as iva, 
	vt.fecha_registro 
from venta_detalle vd
inner join venta_total vt on vt.id = vd.id_venta 
inner join productos p on p.id = vd.id_producto 
left outer join clientes c on c.id = vt.id_cliente 
inner join usuarios u on u.id = vt.id_usuario 
where vt.fecha_registro  between '2020-08-01 00:00:00' and '2020-08-31 11:59:59';

# Ventas agrupadas
select vt.id as id_venta, u.nombre_completo as usuario,
	upper(concat(c.rfc, ' ', c.nombre, ' ', c.ap_paterno, ' ', c.ap_materno )) as cliente,
	p.nombre_largo as producto,
	sum(vd.cantidad) as cantidad, sum(vd.precio) as precio,
	sum(vd.cantidad * vd.precio) as total, sum(round(((vd.cantidad * vd.precio)/1.16),2)) as subtotal, sum(round( ((vd.cantidad * vd.precio) - ((vd.cantidad * vd.precio)/1.16)),2 )) as iva, 
	vt.fecha_registro 
from venta_detalle vd
inner join venta_total vt on vt.id = vd.id_venta 
inner join productos p on p.id = vd.id_producto 
left outer join clientes c on c.id = vt.id_cliente 
inner join usuarios u on u.id = vt.id_usuario 
where vt.fecha_registro  between '2020-08-01 00:00:00' and '2020-08-31 11:59:59'
group by vt.id;

-- Total por detalle
select sum(vd.cantidad) as productos_vendidos, sum(vd.precio) as precio_total,
	sum(vd.cantidad * vd.precio) as total, sum(round(((vd.cantidad * vd.precio)/1.16),2)) as subtotal, sum(round( ((vd.cantidad * vd.precio) - ((vd.cantidad * vd.precio)/1.16)),2 )) as iva 
from venta_detalle vd
inner join venta_total vt on vt.id = vd.id_venta 
where vt.fecha_registro  between '2020-08-01 00:00:00' and '2020-08-31 11:59:59';

-- Total general
select sum(vt.total-vt.cambio) as ttotal, sum(vt.sub_total) as tsubtotal, sum(vt.iva) as tiva
from venta_total vt
where vt.fecha_registro  between '2020-08-01 00:00:00' and '2020-08-31 11:59:59';


select p.id, p.nombre_largo, c.unidad, ct.nombre, sum(c.cantidad) as existencia, c.num_piezas #, p.costo, c.costo 
from productos p
inner join compras c on c.id_producto = p.id
inner join catalogos ct on ct.id = c.unidad
group by p.id, p.nombre_largo, c.unidad, ct.nombre, c.num_piezas #, p.costo, c.costo
order by p.id;

select p.id, p.nombre_largo, c.unidad, ct.nombre, c.cantidad 
from productos p
inner join compras c on c.id_producto = p.id
inner join catalogos ct on ct.id = c.unidad
order by p.id;

select * from compras where num_piezas is not null;
select distinct unidad, num_piezas from compras where id_producto = 8;

select * from venta_detalle where id_producto = 8 and unidad = 2 and num_piezas == null;

delete from venta_detalle  where id_venta  = 18;

update venta_detalle set unidad = 2 where unidad is null;
delete from venta_pago where id_venta = 18;
delete from venta_total where id = 18;


select num_piezas from compras where unidad = 2;
update compras set num_piezas = null where unidad = 2;

select num_piezas from venta_detalle where unidad = 2;
update venta_detalle set num_piezas = null where unidad = 2;

select fecha_registro from venta_total where id = 15; ## 2020-09-16 20:33:55.0
select * from venta_detalle where id_venta  = 15;

select * from 

##select id_venta from venta_detalle where id_producto = 8;

select id, costo, unidad from compras where id_producto = 8  order by fecha_registro desc;
select id, costo, unidad from compras where id_producto = 8 and fecha_registro < '2020-09-16 20:33:55.0' order by fecha_registro desc;

##############################################
##############################################
## ALTERS NUEVA VERSION PRECIOSO 05/10/2020
##############################################
##############################################
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


truncate sesion;
select * from sesion;
update sesion set salida = null where id = 1;

select * from venta_total;
update venta_total set id_cliente = null, folio_factura = null where id = 1;


select total, sub_total, iva from venta_total where id = 2;
select * from venta_detalle where id_venta = 2;
select * from venta_pago where id_venta = 2;

update venta_detalle set cantidad = 1 where id = 3;

update venta_total set total = 218, sub_total = 183.12, iva = 34.88 where id = 2;
update venta_pago set monto = 218 where id = 2;

select * from venta_pago;

truncate venta_pago ;
truncate venta_detalle ;
truncate venta_total ;

update venta_total set descuento = null where descuento = 0;

insert into sesion (id_usuario, entrada, monto_inicial, retiro) values (2, '2020-10-09 09:27:33.0', 500, '2020-10-09 09:27:33.0');
update sesion set salida = null where id = 4;
update sesion set salida = null, retiro = '2020-10-09 09:27:33.0' where id = 4;

truncate sesion;
