/* select all rows by date between two dates and order by flat id and date */
SELECT * FROM calendarCache WHERE date(date) BETWEEN date('2013-08-01') AND date('2013-11-01') ORDER BY fid,date(date);

SELECT * FROM calendarCache c,flat f WHERE date(c.date) BETWEEN date('2013-08-01') AND date('2013-11-01') ORDER BY f.position,date(c.date);


SELECT c.* FROM calendarCache c WHERE date(c.date) BETWEEN date('2013-08-01') AND date('2013-11-01') ORDER BY date(c.date) INNER JOIN flat
ON flat.fid=calendarCache.fid;

SELECT c.* FROM calendarCache c INNER JOIN flat ON flat.fid=c.fid ;

SELECT c.* FROM calendarCache c INNER JOIN flat ON flat.fid=c.fid WHERE flat.active = '1';

SELECT c.* FROM calendarCache c INNER JOIN flat ON flat.fid=c.fid WHERE flat.active = '1' AND date(c.date) BETWEEN date('2013-08-01') AND date('2013-11-01') ORDER BY flat.position;

INSERT into calendarCache (date,fid) values (date('2012-11-12'),1);

SELECT c.* FROM calendarCache c INNER JOIN flat f ON f.fid=c.fid WHERE f.active = '1' AND date(c.date) BETWEEN date('2013-08-01') AND date('2013-11-01') ORDER BY f.position, date(c.date);

SELECT * FROM Cleanings c INNER JOIN maiden m ON c.eid=m.eid WHERE c.date IN (SELECT date from Cleanings WHERE cid IN (1,5)) ORDER BY c.date;

SELECT p.*,m.amount  FROM payment p INNER JOIN money m, intervalMoney i ON p.mid = m.mid AND i.mid = m.mid WHERE i.iid = 1;

/* get all cleanings that get in interval */
SELECT c.*,m.background FROM cleanings c, interval i,maiden m WHERE c.date BETWEEN i.date_from AND i.date_to AND i.iid = '1' AND m.eid=c.eid ORDER BY c.date;

/* get new background after removing cleaning */
SELECT m.background FROM cleanings c INNER JOIN maiden m ON m.eid=c.eid WHERE c.date = (SELECT c.date FROM cleanings c WHERE c.cid='1') AND c.cid != '1' ORDER BY c.cid DESC LIMIT 1;

/* calculates new background. If count(*) > 1 then returns -2 and if count(*)==0 than returns 0 */
SELECT CASE
WHEN count(background) > 1
THEN -2
WHEN count(background) = 0
THEN 0
ELSE background
END AS background
FROM (
(SELECT background FROM cleanings as c INNER JOIN maiden m ON m.eid=c.eid
WHERE c.date = (SELECT c.date FROM cleanings c WHERE c.cid='4')
AND c.cid != '4')
)

/* updates background in cache if cleanings was added and sets background = -2 if count(*) > 1 */
UPDATE calendarCache
SET background = (
SELECT CASE
WHEN count(background) > 1
THEN -2
WHEN count(background) = 0
THEN 0
ELSE background
END AS background
FROM (
(SELECT background FROM cleanings as c INNER JOIN maiden m ON m.eid=c.eid
WHERE c.date = (SELECT c.date FROM cleanings c WHERE c.cid = '1')
AND c.fid = (SELECT c.fid FROM cleanings c WHERE c.cid = '1'))
))
WHERE fid = (SELECT c.fid as fid FROM cleanings c WHERE c.cid = '1')
AND date = (SELECT c.date as date FROM cleanings c WHERE c.cid = '1');

/* updates background in cache if cleaning will be removed and sets background = -2 if count(*) > 1 */
UPDATE calendarCache
SET background = (
SELECT CASE
WHEN count(background) > 1
THEN -2
WHEN count(background) = 0
THEN 0
ELSE background
END AS background
FROM (
(SELECT background FROM cleanings as c INNER JOIN maiden m ON m.eid=c.eid
WHERE c.date = (SELECT c.date FROM cleanings c WHERE c.cid = '1')
AND c.fid = (SELECT c.fid FROM cleanings c WHERE c.cid = '1')
AND c.cid != '1')
))
WHERE fid = (SELECT c.fid as fid FROM cleanings c WHERE c.cid = '1')
AND date = (SELECT c.date as date FROM cleanings c WHERE c.cid = '1');

/* updates background in cache by intervalID and date and sets background = -2 if count(*) > 1 */
UPDATE calendarCache
SET background = (
SELECT CASE
WHEN count(background) > 1
THEN -2
WHEN count(background) = 0
THEN 0
ELSE background
END AS background
FROM ((
SELECT background FROM cleanings as c INNER JOIN maiden m ON m.eid=c.eid
WHERE c.date = '20130818'
AND c.iid = '1')
))
WHERE fid = (SELECT c.fid as fid FROM cleanings c WHERE c.iid = '1' LIMIT 1)
AND date = '20130818';

/* gets a lot of interval's data by eviction date */
SELECT ia.*,i.date_from,i.date_to,f.address,f.short_address,f.fid,p.price,m.amount,
CASE WHEN (SELECT COUNT(*) FROM cleanings c WHERE c.iid=i.iid) > 1 THEN -2
WHEN (SELECT COUNT(*) FROM cleanings c WHERE c.iid=i.iid) = 0 THEN 0
ELSE (SELECT m.background FROM cleanings c INNER JOIN maiden m ON m.eid=c.eid WHERE c.iid=i.iid)
END AS background
FROM intervalAnketa ia
INNER JOIN interval i, intervalFlat if, flat f, intervalMoney im,money m,payment p
ON ia.iid = i.iid AND ia.iid = if.iid AND if.fid = f.fid AND im.mid = p.mid AND im.iid = i.iid AND m.mid = p.mid
WHERE i.date_to = '20130818' ORDER BY position

/* seaches interval by anketa */
SELECT ia.*,i.date_from,i.date_to,f.address,f.short_address,f.fid,p.price,m.amount, CASE WHEN count(m.background) > 1 THEN -2 WHEN count(m.background) = 0 THEN 0 ELSE m.background END AS background FROM intervalAnketa ia INNER JOIN interval i, intervalFlat if, flat f, intervalMoney im,money m,payment p,maiden ma, cleanings c ON ia.iid = i.iid AND ia.iid = if.iid AND if.fid = f.fid AND im.mid = p.mid AND im.iid = i.iid AND m.mid = p.mid AND ma.eid = c.eid AND c.iid=i.iid WHERE ia.telephone_left LIKE ? OR ia.telephone_right LIKE ? OR ia.name_left LIKE ? OR ia.name_right LIKE ? OR ia.anketa LIKE ? OR ia.else_info LIKE ? GROUP BY c.iid

/* get cleanings that will remain after deleting interval */
SELECT c.cid,c.fid,c.date,m.background FROM cleanings c INNER JOIN interval i,maiden m,intervalFlat if ON if.fid=c.fid AND if.iid=i.iid WHERE c.date BETWEEN i.date_from AND i.date_to AND i.iid = '5' AND  c.iid != i.iid AND m.eid=c.eid AND c.fid=if.fid GROUP BY c.date,m.background ORDER BY c.date,m.background;

/* get other cleanings in the days of interval's cleanings */
SELECT c.*,m.background FROM cleanings c INNER JOIN intervalFlat if,maiden m ON c.fid=if.fid AND m.eid=c.eid WHERE c.date IN (SELECT c.date FROM cleanings c WHERE c.iid = '3') AND if.iid='3' AND c.iid != if.iid GROUP BY c.date,m.background ORDER BY c.date,m.background;

/* length of interval in days */
SELECT COUNT(c.date)-1 FROM calendarCache c INNER JOIN intervalFlat if, interval i ON if.fid = c.fid AND i.iid = if.iid WHERE i.iid = 1 AND c.date BETWEEN i.date_from AND i.date_to;

/* cache days that get in interval */
SELECT c.date FROM calendarCache c INNER JOIN intervalFlat if, interval i ON if.fid = c.fid AND i.iid = if.iid WHERE i.iid = 1 AND c.date BETWEEN i.date_from AND i.date_to;

/* set debt by intervalID */
UPDATE calendarCache SET debt = '999' WHERE date >= (SELECT i.date_from FROM interval i WHERE i.iid = '1') AND date <=  (SELECT i.date_to FROM interval i WHERE i.iid = '1') AND fid = (SELECT if.fid FROM intervalFlat if WHERE if.iid = '1');

/* select debt at day without interval id*/
SELECT d.*,m.amount FROM money m INNER JOIN debt d, interval i, intervalMoney im ON d.mid=m.mid AND d.mid=im.mid AND im.iid=i.iid WHERE i.date_from <= ? AND i.date_to >= ? AND i.iid != ? ORDER BY m.timestamp DESC;

/* select intervals that get to custom interval */
SELECT i.iid FROM interval i,(SELECT i.iid as interval,if.fid as flat, i.date_from as start,i.date_to as finish FROM interval i INNER JOIN intervalFlat if ON if.iid=i.iid WHERE i.iid = 3) date INNER JOIN intervalFlat if ON if.iid=i.iid WHERE (start <= i.date_from OR start <= i.date_to) AND (finish >= i.date_from OR finish >= i.date_to) AND if.fid = date.flat AND i.iid !=date.interval;

/* select intervals and there debts */
SELECT i.iid,m.amount FROM interval i INNER JOIN money m, debt d,intervalMoney im ON m.mid=d.mid AND im.iid=i.iid AND im.mid=m.mid;

/* select intervals that get to custom interval and there debts */
SELECT i.iid,m.amount FROM interval i,(SELECT i.iid as interval,if.fid as flat, i.date_from as start,i.date_to as finish FROM interval i INNER JOIN intervalFlat if ON if.iid=i.iid WHERE i.iid = 3) date INNER JOIN intervalFlat if,money m, debt d,intervalMoney im ON if.iid=i.iid AND m.mid=d.mid AND im.iid=i.iid AND im.mid=m.mid WHERE (start <= i.date_from OR start <= i.date_to) AND (finish >= i.date_from OR finish >= i.date_to) AND if.fid = date.flat AND i.iid !=date.interval;

/* select other intervals and there days in cache that get in interval after deleting interval */
SELECT i.iid,c.date FROM calendarCache c INNER JOIN intervalFlat if, interval i ON if.fid = c.fid AND i.iid = if.iid WHERE i.iid IN (SELECT i.iid FROM interval i,(SELECT i.iid as interval,if.fid as flat, i.date_from as start,i.date_to as finish FROM interval i INNER JOIN intervalFlat if ON if.iid=i.iid WHERE i.iid = '5') date INNER JOIN intervalFlat if ON if.iid=i.iid WHERE (start <= i.date_from OR start <= i.date_to) AND (finish >= i.date_from OR finish >= i.date_to) AND if.fid = date.flat AND i.iid !=date.interval)  AND c.date BETWEEN i.date_from AND i.date_to AND c.date >= (SELECT i.date_from FROM interval i WHERE i.iid = '5') AND date <=  (SELECT i.date_to FROM interval i WHERE i.iid = '5') ORDER BY c.date;

/* get debt sum by day and intervals after deleting interval */
SELECT l.day as date,l.flat as fid,r.debt as debt FROM (SELECT if.fid as flat, c.date as day FROM calendarCache c INNER JOIN intervalFlat if, interval i ON if.fid = c.fid AND i.iid = if.iid WHERE i.iid = 3 AND c.date BETWEEN i.date_from AND i.date_to)  as l LEFT OUTER JOIN (SELECT c.date as day, SUM(m.amount) as debt FROM calendarCache c INNER JOIN intervalMoney im, money m, debt d, intervalFlat if, interval i ON i.iid=im.iid AND im.mid=m.mid AND m.mid=d.mid AND if.fid = c.fid AND i.iid = if.iid WHERE i.iid IN (SELECT i.iid FROM interval i,(SELECT i.iid as interval,if.fid as flat, i.date_from as start,i.date_to as finish FROM interval i INNER JOIN intervalFlat if ON if.iid=i.iid WHERE i.iid = '3') date INNER JOIN intervalFlat if ON if.iid=i.iid WHERE (start <= i.date_from OR start <= i.date_to) AND (finish >= i.date_from OR finish >= i.date_to) AND if.fid = date.flat AND i.iid !=date.interval)  AND c.date BETWEEN i.date_from AND i.date_to AND c.date >= (SELECT i.date_from FROM interval i WHERE i.iid = '3') AND date <=  (SELECT i.date_to FROM interval i WHERE i.iid = '3') GROUP BY c.date ORDER BY c.date,m.timestamp) as r ON l.day=r.day;

/* get existing debt sum by dayd and interval*/
SELECT l.day as date,l.flat as fid,r.debt as debt FROM (SELECT if.fid as flat, c.date as day FROM calendarCache c INNER JOIN intervalFlat if, interval i ON if.fid = c.fid AND i.iid = if.iid WHERE i.iid = 3 AND c.date BETWEEN i.date_from AND i.date_to)  as l LEFT OUTER JOIN (SELECT c.date as day, SUM(m.amount) as debt FROM calendarCache c INNER JOIN intervalMoney im, money m, debt d, intervalFlat if, interval i ON i.iid=im.iid AND im.mid=m.mid AND m.mid=d.mid AND if.fid = c.fid AND i.iid = if.iid WHERE i.iid IN (SELECT i.iid FROM interval i,(SELECT i.iid as interval,if.fid as flat, i.date_from as start,i.date_to as finish FROM interval i INNER JOIN intervalFlat if ON if.iid=i.iid WHERE i.iid = '3') date INNER JOIN intervalFlat if ON if.iid=i.iid WHERE (start <= i.date_from OR start <= i.date_to) AND (finish >= i.date_from OR finish >= i.date_to) AND if.fid = date.flat)  AND c.date BETWEEN i.date_from AND i.date_to AND c.date >= (SELECT i.date_from FROM interval i WHERE i.iid = '3') AND date <=  (SELECT i.date_to FROM interval i WHERE i.iid = '3') GROUP BY c.date ORDER BY c.date,m.timestamp) as r ON l.day=r.day;

/* get prepay sum by day and intervals after deleting interval */
SELECT l.day as date,l.flat as fid,r.prepay as prepay FROM (SELECT if.fid as flat, c.date as day FROM calendarCache c INNER JOIN intervalFlat if, interval i ON if.fid = c.fid AND i.iid = if.iid WHERE i.iid = 3 AND c.date BETWEEN i.date_from AND i.date_to)  as l LEFT OUTER JOIN (SELECT c.date as day, SUM(m.amount) as prepay FROM calendarCache c INNER JOIN intervalMoney im, money m, prepay p, intervalFlat if, interval i ON i.iid=im.iid AND im.mid=m.mid AND m.mid=p.mid AND if.fid = c.fid AND i.iid = if.iid WHERE i.iid IN (SELECT i.iid FROM interval i,(SELECT i.iid as interval,if.fid as flat, i.date_from as start,i.date_to as finish FROM interval i INNER JOIN intervalFlat if ON if.iid=i.iid WHERE i.iid = '3') date INNER JOIN intervalFlat if ON if.iid=i.iid WHERE (start <= i.date_from OR start <= i.date_to) AND (finish >= i.date_from OR finish >= i.date_to) AND if.fid = date.flat AND i.iid !=date.interval)  AND c.date BETWEEN i.date_from AND i.date_to AND c.date >= (SELECT i.date_from FROM interval i WHERE i.iid = '3') AND date <=  (SELECT i.date_to FROM interval i WHERE i.iid = '3') GROUP BY c.date ORDER BY c.date,m.timestamp) as r ON l.day=r.day;

/* get existing prepay sum by dayd and interval*/
SELECT l.day as date,l.flat as fid,r.prepay as prepay FROM (SELECT if.fid as flat, c.date as day FROM calendarCache c INNER JOIN intervalFlat if, interval i ON if.fid = c.fid AND i.iid = if.iid WHERE i.iid = 3 AND c.date BETWEEN i.date_from AND i.date_to)  as l LEFT OUTER JOIN (SELECT c.date as day, SUM(m.amount) as prepay FROM calendarCache c INNER JOIN intervalMoney im, money m, prepay p, intervalFlat if, interval i ON i.iid=im.iid AND im.mid=m.mid AND m.mid=p.mid AND if.fid = c.fid AND i.iid = if.iid WHERE i.iid IN (SELECT i.iid FROM interval i,(SELECT i.iid as interval,if.fid as flat, i.date_from as start,i.date_to as finish FROM interval i INNER JOIN intervalFlat if ON if.iid=i.iid WHERE i.iid = '3') date INNER JOIN intervalFlat if ON if.iid=i.iid WHERE (start <= i.date_from OR start <= i.date_to) AND (finish >= i.date_from OR finish >= i.date_to) AND if.fid = date.flat)  AND c.date BETWEEN i.date_from AND i.date_to AND c.date >= (SELECT i.date_from FROM interval i WHERE i.iid = '3') AND date <=  (SELECT i.date_to FROM interval i WHERE i.iid = '3') GROUP BY c.date ORDER BY c.date,m.timestamp) as r ON l.day=r.day;



/* gets profit by day in date interval */
SELECT c.date,sum(m.amount*1.0/days.num) as sum,days.num FROM calendarCache c
INNER JOIN interval i, intervalFlat if, intervalMoney im, money m, payment p,
(SELECT i.iid as iid,COUNT(*)-1 as num
FROM calendarCache c
INNER JOIN intervalFlat if, interval i ON if.fid = c.fid AND i.iid = if.iid
WHERE ('20130701' <= i.date_from OR '20130701' <= i.date_to)
AND ('20130731' >= i.date_from OR '20130731' >= i.date_to)
AND if.fid = '1'
AND c.date BETWEEN i.date_from AND i.date_to
GROUP BY i.iid) days
ON if.fid = c.fid AND i.iid = if.iid AND im.iid=i.iid AND im.mid = m.mid AND m.mid = p.mid AND days.iid=i.iid
WHERE i.iid = days.iid
AND c.date BETWEEN '20130701' AND '20130731'
AND c.date BETWEEN i.date_from AND i.date_to
GROUP BY c.date ORDER BY c.date;

/* gets profit between two days in flat before today */
SELECT sum(profit.sum) FROM (SELECT c.date,sum(m.amount*1.0/days.num) as sum,days.num FROM calendarCache c
INNER JOIN interval i, intervalFlat if, intervalMoney im, money m, payment p,
(SELECT i.iid as iid,COUNT(*)-1 as num
FROM calendarCache c
INNER JOIN intervalFlat if, intervalAnketa ia, interval i ON if.fid = c.fid AND i.iid = if.iid AND ia.iid=i.iid
WHERE ('20130801' <= i.date_from OR '20130801' <= i.date_to)
AND ('20130830' >= i.date_from OR '20130830' >= i.date_to)
AND if.fid = '1'
AND c.date BETWEEN i.date_from AND i.date_to
AND ia.type = 2
GROUP BY i.iid) days
ON if.fid = c.fid AND i.iid = if.iid AND im.iid=i.iid AND im.mid = m.mid AND m.mid = p.mid AND days.iid=i.iid
WHERE i.iid = days.iid
AND c.date BETWEEN '20130801' AND '20130830'
AND c.date >= i.date_from AND c.date < i.date_to
AND c.date <= '20130810'
GROUP BY c.date ORDER BY c.date) as profit;

/* gets full profit */
SELECT sum(profit.sum) FROM (SELECT c.date,sum(m.amount*1.0/days.num) as sum,days.num FROM calendarCache c
INNER JOIN interval i, intervalFlat if, intervalMoney im, money m, payment p,
(SELECT i.iid as iid,COUNT(*)-1 as num
FROM calendarCache c
INNER JOIN intervalFlat if, intervalAnketa ia, interval i ON if.fid = c.fid AND i.iid = if.iid AND ia.iid=i.iid
WHERE if.fid = '1'
AND c.date BETWEEN i.date_from AND i.date_to
AND ia.type = 2
GROUP BY i.iid) days
ON if.fid = c.fid AND i.iid = if.iid AND im.iid=i.iid AND im.mid = m.mid AND m.mid = p.mid AND days.iid=i.iid
WHERE i.iid = days.iid
AND c.date >= i.date_from AND c.date < i.date_to
AND c.date <= '20130831'
GROUP BY c.date ORDER BY c.date) as profit;