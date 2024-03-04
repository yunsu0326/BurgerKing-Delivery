
show user;
--USER이(가) "SEMI_ORAUSER4"입니다.

create table tbl_박윤수
(no number
,name varchar2(20)
);

insert into tbl_박윤수(no, name) values(11, '하하하');
--1 행 이(가) 삽입되었습니다.

commit;

create table tbl_member     
(userid             varchar2(40)   not null  -- 회원아이디
,pwd                varchar2(200)  not null  -- 비밀번호 (SHA-256 암호화 대상)
,name               varchar2(30)   not null  -- 회원명
,email              varchar2(200)  not null  -- 이메일 (AES-256 암호화/복호화 대상)
,mobile             varchar2(200)            -- 연락처 (AES-256 암호화/복호화 대상) 
,postcode           varchar2(5)              -- 우편번호
,address            varchar2(200)            -- 주소
,detailaddress      varchar2(200)            -- 상세주소
,extraaddress       varchar2(200)            -- 참고항목
,gender             varchar2(1)              -- 성별   남자:1  / 여자:2
,birthday           varchar2(10)             -- 생년월일   
,coin               number default 0         -- 코인액
,point              number default 0         -- 포인트 
,registerday        date default sysdate     -- 가입일자 
,lastpwdchangedate  date default sysdate     -- 마지막으로 암호를 변경한 날짜  
,status             number(1) default 1 not null     -- 회원탈퇴유무   1: 사용가능(가입중) / 0:사용불능(탈퇴) 
,idle               number(1) default 0 not null     -- 휴면유무      0 : 활동중  /  1 : 휴면중 
,constraint PK_tbl_member_userid primary key(userid)
,constraint UQ_tbl_member_email  unique(email)
,constraint CK_tbl_member_gender check( gender in('1','2') )
,constraint CK_tbl_member_status check( status in(0,1) )
,constraint CK_tbl_member_idle check( idle in(0,1) )
);
-- Table TBL_MEMBER이(가) 생성되었습니다.

create table tbl_loginhistory
(historyno   number
,fk_userid   varchar2(40) not null  -- 회원아이디
,logindate   date default sysdate not null -- 로그인되어진 접속날짜및시간
,clientip    varchar2(20) not null
,constraint  PK_tbl_loginhistory primary key(historyno)
,constraint  FK_tbl_loginhistory_fk_userid foreign key(fk_userid) references tbl_member(userid)
);
-- Table TBL_LOGINHISTORY이(가) 생성되었습니다.

create sequence seq_historyno
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

select *
from tbl_member
order by registerday desc;

select *
from img;

select count(*)
from event;

select userid, name, email, mobile, postcode, address, detailaddress, extraaddress, gender 
        , birthday, coin, point, to_char(registerday, 'yyyy-mm-dd') AS registerday 
from tbl_member 
where status = 1 and userid = 'admin';


---- *** 제품 테이블 : tbl_product *** ----
-- drop table tbl_product purge; 
create table tbl_product
(pnum           number(8) not null       -- 제품번호(Primary Key)
,pname          varchar2(100) not null   -- 제품명
,fk_cnum        number(8)                -- 카테고리코드(Foreign Key)의 시퀀스번호 참조
,pimage        varchar2(100) default 'noimage.png' -- 이미지파일명
,pqty           number(8) default 0      -- 제품 재고량
,price          number(8) default 0      -- 제품 정가
,pcontent       varchar2(2000)           -- 제품설명  
,pdetail        varchar2(1000)           -- 제품구성                          
                                        
,constraint  PK_tbl_product_pnum primary key(pnum)
,constraint  FK_tbl_product_fk_cnum foreign key(fk_cnum) references tbl_category(cnum)
);

-- drop sequence seq_tbl_product_pnum;
create sequence seq_tbl_product_pnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

-- 아래는 fk_snum 컬럼의 값이 1 인 'HIT' 상품만 입력한 것임. 
insert into tbl_product(pnum, pname, fk_cnum, pcompany, pimage1, pimage2, pqty, price, saleprice, fk_snum, pcontent, point)
values(seq_tbl_product_pnum.nextval, '스마트TV', 1, '삼성', 'tv_samsung_h450_1.png','tv_samsung_h450_2.png', 100,1200000,800000, 1,'42인치 스마트 TV. 기능 짱!!', 50);

ALTER TABLE tbl_product ADD pdetail varchar2(1000);

select *
from tbl_product

select *
from tbl_category



-----------------------------------------------------------------------------------------
 ------------------ >>> 주문관련 테이블 <<< -----------------------------
-- [1] 주문 테이블    : tbl_order
-- [2] 주문상세 테이블 : tbl_orderdetail


-- *** "주문" 테이블 *** --
create table tbl_order
(odrcode        varchar2(20) not null          -- 주문코드(명세서번호)  주문코드 형식 : s+날짜+sequence ==> s20220503-1 , s20220503-2 , s20220503-3
                                               --                                                  s20220504-4 , s20220504-5 , s20220504-6
,fk_userid      varchar2(20) not null          -- 사용자ID
,odrtotalPrice  number       not null          -- 주문총액
,odrtotalPoint  number       not null          -- 주문총포인트
,odrdate        date default sysdate not null  -- 주문일자
,deliveryaddress varchar(200)
,orderrequest varchar(200)
,constraint PK_tbl_order_odrcode primary key(odrcode)
,constraint FK_tbl_order_fk_userid foreign key(fk_userid) references tbl_member(userid)
);
--Table TBL_ORDER이(가) 생성되었습니다.

-- "주문코드(명세서번호) 시퀀스" 생성
create sequence seq_tbl_order
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
--Sequence SEQ_TBL_ORDER이(가) 생성되었습니다.
select 's'||to_char(sysdate,'yyyymmdd')||'-'||seq_tbl_order.nextval AS odrcode
from dual;
-- s20231101-1

select odrcode, fk_userid, 
       odrtotalPrice, odrtotalPoint,
       to_char(odrdate, 'yyyy-mm-dd hh24:mi:ss') as odrdate
from tbl_order
order by odrcode desc;


-- *** "주문상세" 테이블 *** --
create table tbl_orderdetail
(odrseqnum      number               not null   -- 주문상세 일련번호
,fk_odrcode     varchar2(20)         not null   -- 주문코드(명세서번호)
,fk_pnum        number(8)            not null   -- 제품번호
,oqty           number               not null   -- 주문량
,odrprice       number               not null   -- "주문할 그때 그당시의 실제 판매가격" ==> insert 시 tbl_product 테이블에서 해당제품의 saleprice 컬럼값을 읽어다가 넣어주어야 한다.
,deliverStatus  number(1) default 1  not null   -- 배송상태( 1 : 주문만 받음,  2 : 배송중,  3 : 배송완료)
,deliverDate    date                            -- 배송완료일자  default 는 null 로 함.
,constraint PK_tbl_orderdetail_odrseqnum  primary key(odrseqnum)
,constraint FK_tbl_orderdetail_fk_odrcode foreign key(fk_odrcode) references tbl_order(odrcode) on delete cascade
,constraint FK_tbl_orderdetail_fk_pnum foreign key(fk_pnum) references tbl_product(pnum)
,constraint CK_tbl_orderdetail check( deliverStatus in(1, 2, 3) )
);
--Table TBL_ORDERDETAIL이(가) 생성되었습니다.

-- "주문상세 일련번호 시퀀스" 생성
create sequence seq_tbl_orderdetail
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
--Sequence SEQ_TBL_ORDERDETAIL이(가) 생성되었습니다.


--------------------------------------------------------------------------------------
insert into tbl_product(pnum, pname, fk_cnum, pimage, pqty, price, pcontent, pdetail)
values(seq_tbl_product_pnum.nextval, "트머와팩(2인)", 1, "트머와팩(2)인.png", 100, 20300, "트러플소스 2배로 더욱 깊어진 풍미! 네 가지 머쉬룸이 선사하는 깊고 풍부한 맛의 향연", "트러플머쉬룸와퍼+트러플머쉬룸와퍼주니어 +크리미모짜볼5조각+F/F(L)+콜라(R)2");


insert into tbl_product(pnum, pname, fk_cnum, pimage, pqty, price, pcontent, pdetail)
values(seq_tbl_product_pnum.nextval, '트머와 콤비팩(2인)', 1, '트머와 콤비팩(2인).png', 100, 22800, '트러플소스 2배로 더욱 깊어진 풍미! 네 가지 머쉬룸이 선사하는 깊고 풍부한 맛의 향연', '트러플머쉬룸와퍼+갈릭불고기와퍼 +크리미모짜볼5조각+F/F(L)+콜라(R)2');

insert into tbl_product(pnum, pname, fk_cnum, pimage, pqty, price, pcontent, pdetail)
values(seq_tbl_product_pnum.nextval, '트머와 트리플팩(3인)', 1, '트머와 트리플팩(3인).png', 100, 30600, '트러플소스 2배로 더욱 깊어진 풍미! 네 가지 머쉬룸이 선사하는 깊고 풍부한 맛의 향연', '트러플머쉬룸와퍼+콰트로치즈와퍼+통새우와퍼주니어 +크리미모짜볼5조각+F/F(L)+콜라(R)3');

insert into tbl_product(pnum, pname, fk_cnum, pimage, pqty, price, pcontent, pdetail)
values(seq_tbl_product_pnum.nextval, '트머와 올인팩(3인)', 1, '트머와 올인팩(3인).png', 100, 35100, '트러플소스 2배로 더욱 깊어진 풍미! 네 가지 머쉬룸이 선사하는 깊고 풍부한 맛의 향연', '트러플머쉬룸와퍼+더블트러플머쉬룸와퍼 +트러플머쉬룸와퍼주니어 +크리미모짜볼5조각+F/F(L)+콜라(R)3');

commit;


insert into tbl_product(pnum, pname, fk_cnum, pimage, pqty, price, pcontent, pdetail)
values(seq_tbl_product_pnum.nextval, '트러플머쉬룸와퍼세트+복숭아컵', 2, '트러플머쉬룸와퍼세트+복숭아컵.png', 100, 15000, null, null);

insert into tbl_product(pnum, pname, fk_cnum, pimage, pqty, price, pcontent, pdetail)
values(seq_tbl_product_pnum.nextval, '더블트러플머쉬룸와퍼세트+복숭아컵', 2, '더블트러플머쉬룸와퍼세트+복숭아컵.png', 100, 13000, null, null);

insert into tbl_product(pnum, pname, fk_cnum, pimage, pqty, price, pcontent, pdetail)
values(seq_tbl_product_pnum.nextval, '더블트러플머쉬룸와퍼', 2, '더블트러플머쉬룸와퍼.png', 100, 11400, '트러플소스 2배로 더욱 깊어진 풍미! 100% 순쇠고기 패티도 2배!', '단품');

insert into tbl_product(pnum, pname, fk_cnum, pimage, pqty, price, pcontent, pdetail)
values(seq_tbl_product_pnum.nextval, '트러플머쉬룸와퍼', 2, '트러플머쉬룸와퍼.png', 100, 9400, '트러플소스 2배로 더욱 깊어진 풍미! 100% 순쇠고기 패티도 2배!', '단품');

insert into tbl_product(pnum, pname, fk_cnum, pimage, pqty, price, pcontent, pdetail)
values(seq_tbl_product_pnum.nextval, '트러플머쉬룸와퍼주니어', 2, '트러플머쉬룸와퍼주니어.png', 100, 6400, '트러플소스 2배로 더욱 깊어진 풍미! 100% 순쇠고기 패티도 2배!', '단품');

insert into tbl_product(pnum, pname, fk_cnum, pimage, pqty, price, pcontent, pdetail)
values(seq_tbl_product_pnum.nextval, '더블비프불고기버거', 2, '더블비프불고기버거.png', 100, 5900, '달콤한 불고기 소스에 더블 패티로 더욱 깊어진 감칠맛!', '단품');

-------- **** 장바구니 테이블 생성하기 **** ----------
 desc tbl_member;
 desc tbl_product;

 create table tbl_cart
 (cartno        number               not null   --  장바구니 번호             
 ,fk_userid     varchar2(20)         not null   --  사용자ID            
 ,fk_pnum       number(8)            not null   --  제품번호                
 ,oqty          number(8) default 0  not null   --  주문량                   
 ,registerday   date default sysdate            --  장바구니 입력날짜
 ,constraint PK_shopping_cart_cartno primary key(cartno)
 ,constraint FK_shopping_cart_fk_userid foreign key(fk_userid) references tbl_member(userid) 
 ,constraint FK_shopping_cart_fk_pnum foreign key(fk_pnum) references tbl_product(pnum)
 );


 create sequence seq_tbl_cart_cartno
 start with 1
 increment by 1
 nomaxvalue
 nominvalue
 nocycle
 nocache;

 comment on table tbl_cart
 is '장바구니 테이블';

 comment on column tbl_cart.cartno
 is '장바구니번호(시퀀스명 : seq_tbl_cart_cartno)';

 comment on column tbl_cart.fk_userid
 is '회원ID  tbl_member 테이블의 userid 컬럼을 참조한다.';

 comment on column tbl_cart.fk_pnum
 is '제품번호 tbl_product 테이블의 pnum 컬럼을 참조한다.';

 comment on column tbl_cart.oqty
 is '장바구니에 담을 제품의 주문량';

 comment on column tbl_cart.registerday
 is '장바구니에 담은 날짜. 기본값 sysdate';
 
 commit;
 
 select *
 from user_tab_comments;

 select column_name, comments
 from user_col_comments
 where table_name = 'TBL_CART';
 
 select cartno, fk_userid, fk_pnum, oqty, registerday 
 from tbl_cart
 order by cartno asc;
 
 select C.cartno, C.fk_userid, C.fk_pnum, C.oqty, P.pname, P.pimage1, P.saleprice, P.point
 from ( select cartno, fk_userid, fk_pnum, oqty, registerday
        from tbl_cart
        where fk_userid = 'ujkl021') C
 JOIN tbl_product P
 ON C.fk_pnum = P.pnum
 ORDER BY C.cartno DESC;
 
 select  nvl(sum(C.oqty * P.saleprice),0) AS SUMTOTALPRICE
        , nvl(sum(C.oqty * P.point),0) AS SUMTOTALPOINT
 from ( select fk_pnum, oqty
        from tbl_cart
        where fk_userid = 'leess') C
 JOIN tbl_product P
 ON C.fk_pnum = P.pnum
 
 
select *
from tbl_cart
 
 select*
from tbl_member


select pname
from
(
select *
from tbl_cart
where fk_userid = 'qkrdbstn7'
)C JOIN tbl_product P
ON C.fk_pnum = P.pnum
WHERE ROWNUM = 1;


delete from tbl_product 
where pname = 'qwe';

commit

select*
from tbl_order

UPDATE tbl_member
SET lastpwdchangedate = ADD_MONTHS(lastpwdchangedate, -6)
WHERE userid = 'msh5039';


select*
from tbl_member

commit

select*
from tbl_orderdetail

select*
from tbl_order


select D.odrseqnum 
from tbl_orderdetail D JOIN tbl_order O 
on D.fk_odrcode = O.odrcode 
where D.fk_pnum = 1 and O.fk_userid = 'kimjy'

-------- **** 상품구매 후기 테이블 생성하기 **** ----------
create table tbl_purchase_reviews
(review_seq          number 
,fk_userid           varchar2(20)   not null   -- 사용자ID       
,fk_pnum             number(8)      not null   -- 제품번호(foreign key)
,contents            varchar2(4000) not null
,writeDate           date default sysdate
,constraint PK_purchase_reviews primary key(review_seq)
,constraint UQ_purchase_reviews unique(fk_userid, fk_pnum)
,constraint FK_purchase_reviews_userid foreign key(fk_userid) references tbl_member(userid) on delete cascade 
,constraint FK_purchase_reviews_pnum foreign key(fk_pnum) references tbl_product(pnum) on delete cascade
);

-- 로그인하여 실제 해당 제품을 구매했을 때만 딱 1번만 작성할 수 있는 것. 제품후기를 삭제했을 경우에는 다시 작성할 수 있는 것임. 

create sequence seq_purchase_reviews
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;


----- *** 좋아요, 싫어요 (투표) 테이블 생성하기 *** ----- 
create table tbl_product_like
(fk_userid   varchar2(40) not null 
,fk_pnum     number(8) not null
,constraint  PK_tbl_product_like primary key(fk_userid,fk_pnum)
,constraint  FK_tbl_product_like_userid foreign key(fk_userid) references tbl_member(userid)
,constraint  FK_tbl_product_like_pnum foreign key(fk_pnum) references tbl_product(pnum) on delete cascade
);

create table tbl_product_dislike
(fk_userid   varchar2(40) not null 
,fk_pnum     number(8) not null
,constraint  PK_tbl_product_dislike primary key(fk_userid,fk_pnum)
,constraint  FK_tbl_product_dislike_userid foreign key(fk_userid) references tbl_member(userid)
,constraint  FK_tbl_product_dislike_pnum foreign key(fk_pnum) references tbl_product(pnum) on delete cascade
);


-------- **** 매장찾기(카카오지도) 테이블 생성하기 **** ----------
create table tbl_map 
(storeID       varchar2(20) not null   --  매장id
,storeName     varchar2(100) not null  --  매장명
,storeUrl      varchar2(200)            -- 매장 홈페이지(URL)주소
,storeImg      varchar2(200) not null   -- 매장소개 이미지파일명  
,storeAddress  varchar2(200) not null   -- 매장주소 및 매장전화번호
,lat           number not null          -- 위도
,lng           number not null          -- 경도 
,zindex        number not null          -- zindex 
,constraint PK_tbl_map primary key(storeID)
,constraint UQ_tbl_map_zindex unique(zindex)
);
--Table TBL_MAP이(가) 생성되었습니다.

create sequence seq_tbl_map_zindex
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
--Sequence SEQ_TBL_MAP_ZINDEX이(가) 생성되었습니다.

insert into tbl_map(storeID, storeName, storeUrl, storeImg, storeAddress, lat, lng, zindex)
values('store1','롯데백화점 본점','https://place.map.kakao.com/7858517','lotte02.png','서울 중구 을지로 30 (T)02-771-2500',37.56511284953554,126.98187860455485,1);



insert into tbl_map(storeID, storeName, storeUrl, storeImg, storeAddress, lat, lng, zindex)
values('store1','버거킹 서교동사거리점','https://place.map.kakao.com/723372106','store.png','서울 마포구 양화로 100 1,2층',37.55252627273112,126.91858627042939,1);

insert into tbl_map(storeID, storeName, storeUrl, storeImg, storeAddress, lat, lng, zindex)
values('store2','버거킹 마포구청역점','https://place.map.kakao.com/609361648','store.png','서울 마포구 월드컵로 155 1,2층 (우)03962',37.5613819350252,126.90500041831764,2);

insert into tbl_map(storeID, storeName, storeUrl, storeImg, storeAddress, lat, lng, zindex)
values('store3','버거킹 연세로점','https://place.map.kakao.com/21556767','store.png','서울 서대문구 연세로 25 (우)03788', 37.55782624820659,126.93673691393441,3);

insert into tbl_map(storeID, storeName, storeUrl, storeImg, storeAddress, lat, lng, zindex)
values('store4','버거킹 신촌1점','https://place.map.kakao.com/8375653','store.png','서울 서대문구 신촌로 121 1층 (우)03780',37.556106676721626,126.93927630220722,4);

insert into tbl_map(storeID, storeName, storeUrl, storeImg, storeAddress, lat, lng, zindex)
values('store5','버거킹 연희점','https://place.map.kakao.com/22106031','store.png','서울 서대문구 연희로 97 1,2층 (우)03708',37.566805761473645,126.93064533012475,5);

commit


select*
from TBL_PURCHASE_REVIEWS

select *from TBL_IMG;

DROP SEQUENCE SEQ_PURCHASE_REVIEWS

DROP TABLE TBL_PURCHASE_REVIEWS CASCADE CONSTRAINTS;

create table tbl_main_image
(imgno           number not null
,imgfilename     varchar2(100) not null
,constraint PK_tbl_main_image primary key(imgno)
);
-- Table TBL_MAIN_IMAGE이(가) 생성되었습니다.

create sequence seq_main_image
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence SEQ_MAIN_IMAGE이(가) 생성되었습니다.

insert into tbl_main_image(imgno, imgfilename) values(seq_main_image.nextval, '배너1.png');  
insert into tbl_main_image(imgno, imgfilename) values(seq_main_image.nextval, '배너2.png'); 
insert into tbl_main_image(imgno, imgfilename) values(seq_main_image.nextval, '배너3.png'); 
insert into tbl_main_image(imgno, imgfilename) values(seq_main_image.nextval, '배너4.png');
insert into tbl_main_image(imgno, imgfilename) values(seq_main_image.nextval, '배너5.png'); 

commit;

select * 
from tbl_main_image

select imgfilename 
from tbl_main_image


delete from tbl_main_image
where imgno = 5

commit




SELECT pname 
FROM (
    SELECT *
    FROM tbl_order A
    JOIN tbl_orderdetail B ON A.odrcode = B.fk_odrcode
    WHERE A.FK_USERID = 'ujkl021'
    AND TRUNC(A.odrDate) = TRUNC(SYSDATE) 
)V
JOIN tbl_product C ON C.PNUM = V.fk_PNUM



------------- DB 백업하기 -------------


-- 오라클 계정 생성을 위해서는 SYS 또는 SYSTEM 으로 연결하여 작업을 해야 합니다. [SYS 시작] --
show user;
-- USER이(가) "SYS"입니다.

-- 오라클 계정 생성시 계정명 앞에 c## 붙이지 않고 생성하도록 하겠습니다.
alter session set "_ORACLE_SCRIPT"=true;
-- Session이(가) 변경되었습니다.

-- 오라클 계정명은 MYMVC_USER 이고 암호는 gclass 인 사용자 계정을 생성합니다.
create user semi_orauser4  identified by gclass default tablespace users; 
-- User MYMVC_USER이(가) 생성되었습니다.

-- 위에서 생성되어진 MYMVC_USER 이라는 오라클 일반사용자 계정에게 오라클 서버에 접속이 되어지고,
-- 테이블 생성 등등을 할 수 있도록 여러가지 권한을 부여해주겠습니다.
grant connect, resource, create view, unlimited tablespace to semi_orauser4;
-- Grant을(를) 성공했습니다.





