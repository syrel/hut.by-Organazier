/*-------------------------------------------------------------*/
/*---------------------------- F L A T ------------------------*/
/*-------------------------------------------------------------*/
CREATE TABLE flat (
	fid integer NOT NULL,
	address varchar(255),
	CONSTRAINT flat_pkey PRIMARY KEY (fid)
);

/*-------------------------------------------------------------*/
/*--------------------------- C A C H E -----------------------*/
/*-------------------------------------------------------------*/

CREATE TABLE calendarCache (
	cid integer NOT NULL,
	date datetime NOT NULL,
	fid integer NOT NULL,
	state integer NOT NULL DEFAULT '0',
	background integer NOT NULL DEFAULT '0',
	prepay integer NOT NULL DEFAULT '0',
	debt integer NOT NULL DEFAULT '0',
	casus integer NOT NULL DEFAULT '0',
	CONSTRAINT calendar_cache_pkey PRIMARY KEY (cid) autoincrement,
	CONSTRAINT calendar_cache_fkey FOREIGN KEY (fid)
	REFERENCES flat(fid)  MATCH FULL
	ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT uc_dateFid UNIQUE (date,fid)
);

CREATE TABLE calendarCacheExt (
	cid integer NOT NULL,
	book_am integer NOT NULL DEFAULT '0',
	book_pm integer NOT NULL DEFAULT '0',
	rent_am integer NOT NULL DEFAULT '0',
	rent_pm integer NOT NULL DEFAULT '0',
	book_settlement integer NOT NULL DEFAULT '0',
	book_eviction integer NOT NULL DEFAULT '0',
	rent_settlement integer NOT NULL DEFAULT '0',
	rent_eviction integer NOT NULL DEFAULT '0',
	CONSTRAINT calendar_cache_ext_fkey FOREIGN KEY (cid)
	REFERENCES calendarCache(cid)  MATCH FULL
	ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE state (
	sid integer NOT NULL,
	am integer NOT NULL,
	pm integer NOT NULL,
	settlement integer NOT NULL,
	eviction integer NOT NULL,
	CONSTRAINT state_pkey PRIMARY KEY (sid) autoincrement
);
/*-------------------------------------------------------------*/
/*------------------------ I N T E R V A L --------------------*/
/*-------------------------------------------------------------*/

CREATE TABLE interval (
	iid integer NOT NULL,
	date_from datetime NOT NULL,
	date_to datetime NOT NULL,
	CONSTRAINT interval_pkey PRIMARY KEY (iid)
);

CREATE TABLE intervalAnketa (
	iid integer NOT NULL,
	type integer NOT NULL,
	telephone_left varchar(255) NOT NULL DEFAULT '',
	telephone_right varchar(255) NOT NULL DEFAULT '',
	name_left varchar(255) NOT NULL DEFAULT '',
	name_right varchar(255) NOT NULL DEFAULT '',
	anketa text NOT NULL DEFAULT '',
	settlement varchar(255) NOT NULL DEFAULT '',
	eviction varchar(255) NOT NULL DEFAULT '',
	else_info text NOT NULL DEFAULT '',
	CONSTRAINT interval_anketa_fkey FOREIGN KEY (iid)
	REFERENCES interval(iid) MATCH FULL
	ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE intervalFlat (
	fid integer NOT NULL,
	iid integer NOT NULL,
	CONSTRAINT interval_flat_fid_fkey FOREIGN KEY (fid)
	REFERENCES flat(fid) MATCH FULL ON DELETE CASCADE,
	CONSTRAINT interval_flat_iid_fkey FOREIGN KEY (iid)
	REFERENCES interval(iid) MATCH FULL ON DELETE CASCADE
);
/*-------------------------------------------------------------*/
/*--------------------------- M O N E Y -----------------------*/
/*-------------------------------------------------------------*/

CREATE TABLE money (
	mid integer NOT NULL,
	amount integer,
	CONSTRAINT money_pkey PRIMARY KEY (mid),
	CONSTRAINT money_check CHECK (amount >= '0' OR amount IS NULL)
);

CREATE TABLE payment (
	mid integer NOT NULL UNIQUE,
	price integer,
	CONSTRAINT payment_fkey FOREIGN KEY (mid)
	REFERENCES money(mid) MATCH FULL ON DELETE CASCADE,
	CONSTRAINT payment_check CHECK ((price >= '0' OR price IS NULL))
);

CREATE TABLE debt (
	mid integer NOT NULL UNIQUE,
	CONSTRAINT debt_fkey FOREIGN KEY (mid)
	REFERENCES money(mid) MATCH FULL ON DELETE CASCADE
);

CREATE TABLE prepay (
	mid integer NOT NULL UNIQUE,
	CONSTRAINT prepay_fkey FOREIGN KEY (mid)
	REFERENCES money(mid) MATCH FULL ON DELETE CASCADE
);

CREATE TABLE intervalMoney (
	iid integer NOT NULL,
	mid integer NOT NULL UNIQUE,
	CONSTRAINT interval_money_iid_fkey FOREIGN KEY (iid)
	REFERENCES interval(iid) MATCH FULL ON DELETE CASCADE,
	CONSTRAINT interval_money_mid_fkey FOREIGN KEY (mid)
	REFERENCES money(mid) MATCH FULL ON DELETE CASCADE
);
/*-------------------------------------------------------------*/
/*------------------------- E M P L O Y E E -------------------*/
/*-------------------------------------------------------------*/
CREATE TABLE employee (
	eid integer NOT NULL,
	name varchar(255) NOT NULL,
	CONSTRAINT employee_pkey PRIMARY KEY (eid)
);

CREATE TABLE director (
	eid INTEGER PRIMARY KEY,
	FOREIGN KEY (eid) REFERENCES employee(eid) ON DELETE CASCADE
);

CREATE TABLE manager (
	eid INTEGER PRIMARY KEY,
	FOREIGN KEY (eid) REFERENCES employee(eid) ON DELETE CASCADE
);

CREATE TABLE programmer (
	eid INTEGER PRIMARY KEY,
	FOREIGN KEY (eid) REFERENCES employee(eid) ON DELETE CASCADE
);

CREATE TABLE maiden (
	eid INTEGER PRIMARY KEY,
	mon integer NOT NULL DEFAULT '0',
	tue integer NOT NULL DEFAULT '0',
	wen integer NOT NULL DEFAULT '0',
	thu integer NOT NULL DEFAULT '0',
	fri integer NOT NULL DEFAULT '0',
	sat integer NOT NULL DEFAULT '0',
	sun integer NOT NULL DEFAULT '0',
	FOREIGN KEY (eid) REFERENCES employee(eid) ON DELETE CASCADE
);
/*-------------------------------------------------------------*/
/*--------------------------- M A I D E N ---------------------*/
/*-------------------------------------------------------------*/
CREATE TABLE cleanings (
	iid integer NOT NULL,
	eid integer NOT NULL,
	date datetime NOT NULL,
	CONSTRAINT interval_fkey FOREIGN KEY (iid)
	REFERENCES interval(iid) MATCH FULL ON DELETE CASCADE,
	CONSTRAINT employee_fkey FOREIGN KEY (eid)
	REFERENCES employee(eid) MATCH FULL ON DELETE CASCADE
);
