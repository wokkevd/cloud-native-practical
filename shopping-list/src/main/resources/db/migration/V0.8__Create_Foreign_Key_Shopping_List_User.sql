alter table SHOPPING_LIST
  add constraint user_fk
  foreign key (USER_ID)
  references SHOP_USER (ID)