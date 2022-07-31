# BankOfRyazan
Серьёзный проект онлайн банка, с возможностью транзакций между клиентами, возможностью делать вклады, брать кредиты, инвестировать в драгоценные металлы или валюту.  
Фронтовое оформление на бутстрап 5, немного js. Данные хранятся в MySQL.  
Информация по курсами валют и ценам на драгметаллы приходит с бесплатных api.  
В проекте включен spring security (jbdc аутентификация) - защищена страница "личный кабинет" а также, с помощью тегов <sec:authorize> непосредственно в коде страницы 
введено разграничение на возможность некоторых операций (например, нельзя инвестировать или купить валюту без аутентификации).  

Рассмотрим проект поближе:
Структура проекта по классам:  
![ридми](https://user-images.githubusercontent.com/97405800/182043726-604642de-0c62-4271-871b-68006939d7d0.jpg)  
Обычные слои моделей, репозиториев и сервисов.  
  
Личный кабинет выглядит так:  
![лк](https://user-images.githubusercontent.com/97405800/182044109-0bf8d8ea-4cd1-4b50-9050-01892d88a583.jpg).  
Здесь мы видим навбар наверху( имя и фамилия владельца кабинета, его баланс в рублях и валюте. Если валюта на счету отсутствует, то отображается только баланс в рублях.
У Виктории нет евро, поэтому её данные выглядят по-другому:  
![лк2](https://user-images.githubusercontent.com/97405800/182044235-1e4cf5ef-f177-4998-b431-e9fb54214f9d.jpg)  
Эти данные браузер отображает с помощью spring MVC подхода. Про навбар, и в целом сайт хотел бы сказать, что он адаптивен под любое разрешение экрана.  
Кнопки и информация в навбаре складываются в гамбургер, а карусели выстраиваются в столбец :  
![лк3](https://user-images.githubusercontent.com/97405800/182044450-fa4c6c99-fb5d-44f9-bc8f-98b30a374fe5.jpg)
При нажатии, конечно, выплывает всё что было скрыто :  
![лк4](https://user-images.githubusercontent.com/97405800/182044483-fd0ac05e-9fb3-47fd-b85d-bd1eae3e7663.jpg)  
  
Теперь посмотрим на карусели поближе:  
![лк5](https://user-images.githubusercontent.com/97405800/182044567-645e5aca-9280-4f4d-ba8e-9781de00ccd6.jpg)  
Здесь можно перевести деньги любому клиенту из базы данных этого банка по номеру телефона. Перевод автоматически добавляется в исходящие платежи текущему клиенту  
и во входящие платежи - получателю.  
Следующий элемент карусели - входящие платежи текущего пользователя:  
![лк6](https://user-images.githubusercontent.com/97405800/182044664-f8e31450-5a54-487c-9dd8-05fd3e85ad18.jpg)
Реализовано модальное окно, при нажатии на "входящие платежи" оно появляется.  
И еще элемент карусели транзакций - исходящие платежи, реализовано аналогичное модальное окно. 
![лк7](https://user-images.githubusercontent.com/97405800/182044803-460a4dd2-d1a0-477d-b40d-251009b1d73a.jpg)  
![лк8](https://user-images.githubusercontent.com/97405800/182044834-6130cf3b-763a-4ef9-856c-9dc8ff2afee5.jpg)  
  
    
Дальше карусель кредитов - два вращения - активные кредиты / закрытые кредиты.  
![лк9](https://user-images.githubusercontent.com/97405800/182044914-e057958c-f20f-4c08-840a-7afb3b8a2c9c.jpg)  
При нажатии на ДЕТАЛИ ссылка уведет на другую страницу (СТАРЫЙ ДИЗАЙН, пока не дошли руки переделать):  
![лк10](https://user-images.githubusercontent.com/97405800/182044980-d6832d60-7e25-409d-8d4c-b0ca7155ed7f.jpg)  
Как видно, здесь сразу графы заполнены данными (номер договора, ежемесячный платёж - их естественно руками можно поменять - например, выставить бОльшую сумму)  
справа таблица платежей - при внесении очередного платежа, там отображается соответствующая информация.  
Соответственно в карусели "погашенные кредиты" такая же информация, а в деталях есть различие :  
![лк11](https://user-images.githubusercontent.com/97405800/182045100-b6a9e767-ab57-4397-bac2-15e2e575e8c9.jpg)  
Как видим, здесь уже нельзя ничего платить и вносить, просто обозначены данные платежей по этому кредиту.  
  
Перейдём во "вклады"  
![лк12](https://user-images.githubusercontent.com/97405800/182045133-a46bc96e-f9cd-4fff-84e0-38d15c2e4c28.jpg)  
сумма вклада автоматически переводится на баланс, если дата закрытия совпадает с текущей датой, либо текущая дата позже, чем дата закрытия. Статус меняется на "Закрыт".  
  
Ну и "инвестиции" :
![лк13](https://user-images.githubusercontent.com/97405800/182045234-46dce82e-ffd1-4f18-a3b1-6bf8e96a1b25.jpg)  
Отображается сумма инвестиций, во что инвестировали, и маржа.  

Есть готовая страница ИНВЕСТИЦИЙ в самом банке:
![ии1](https://user-images.githubusercontent.com/97405800/182045338-50de82fb-864a-49c3-96b6-9117584b375b.jpg)  
![ии2](https://user-images.githubusercontent.com/97405800/182045365-cb540e89-caf0-40cd-b36e-2605bed715c4.jpg)  
Данные в таблицах автоматически обновляются и пересчитываются.  

Проект в стадии разработки, активно расширяется и улучшается.




