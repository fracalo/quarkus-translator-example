insert into translation_categories (id, name)
values (1, 'test');

insert into translations (id, en, fr, it, category_id)
values (1, 'hello', 'halo', 'ciao', 1),
       (2, 'goodmorning', 'bonjour', 'buongiorno', 1);

alter sequence translation_cat_id_seq RESTART 2;
alter sequence translation_id_seq RESTART 3;
