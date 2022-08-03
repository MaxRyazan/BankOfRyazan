# BankOfRyazan
Серьёзный проект онлайн банка, с возможностью ***транзакций*** между клиентами, возможностью делать ***вклады***, брать ***кредиты***, ***инвестировать*** в драгоценные металлы или валюту. 
***  
##### - Бэкэнд
    - Java 17
    - Spring boot  
    - Spring security  
    - Spring MVC  
##### - Фронтэнд  
    - HTML  
    - CSS  
    - Thymeleaf  
    - Bootstrap 5  
##### - База данных  
    -MySQL
***
##### - Дополнительная информация  
     - Сайт адаптивный, корректно отображается от 600 px ширины экрана  
     - Данные по курсам валют и ценам на драгметаллы  реальные и берутся с бесплатных API
*** 
##### - Новые коммиты
UPD 03.08.22 <a href="#03.08.22">03.08.22</a>
 
##### - Структура проекта по классам:  
     - Обычные слои моделей, репозиториев и сервисов.  
![ридми](https://user-images.githubusercontent.com/97405800/182043726-604642de-0c62-4271-871b-68006939d7d0.jpg)  
  
##### - Личный кабинет:  
    - Здесь мы видим навбар наверху( имя и фамилия владельца кабинета, его баланс в рублях и валюте. 
    Если валюта на счету отсутствует, то отображается только баланс в рублях.
![лк](https://user-images.githubusercontent.com/97405800/182044109-0bf8d8ea-4cd1-4b50-9050-01892d88a583.jpg).  

     - У Виктории нет евро, поэтому её данные выглядят по-другому:  
![лк2](https://user-images.githubusercontent.com/97405800/182044235-1e4cf5ef-f177-4998-b431-e9fb54214f9d.jpg)  
  
    - Кнопки и информация в навбаре складываются в гамбургер, а карусели выстраиваются в столбец :  
![лк3](https://user-images.githubusercontent.com/97405800/182044450-fa4c6c99-fb5d-44f9-bc8f-98b30a374fe5.jpg)
    
    -  При нажатии, конечно, выплывает всё что было скрыто :  
![лк4](https://user-images.githubusercontent.com/97405800/182044483-fd0ac05e-9fb3-47fd-b85d-bd1eae3e7663.jpg)  
  
    - Теперь посмотрим на карусели поближе:  
        # Здесь можно перевести деньги любому клиенту из базы данных этого банка по номеру телефона. 
        Перевод автоматически добавляется в исходящие платежи текущему клиенту и во входящие платежи - получателю.  
        
    - Следующий элемент карусели - входящие платежи текущего пользователя: 
![лк5](https://user-images.githubusercontent.com/97405800/182044567-645e5aca-9280-4f4d-ba8e-9781de00ccd6.jpg)   
![лк6](https://user-images.githubusercontent.com/97405800/182044664-f8e31450-5a54-487c-9dd8-05fd3e85ad18.jpg)

    - Реализовано модальное окно, при нажатии на "входящие платежи" оно появляется.  
    - И еще элемент карусели транзакций - исходящие платежи, реализовано аналогичное модальное окно. 
![лк7](https://user-images.githubusercontent.com/97405800/182044803-460a4dd2-d1a0-477d-b40d-251009b1d73a.jpg)  
![лк8](https://user-images.githubusercontent.com/97405800/182044834-6130cf3b-763a-4ef9-856c-9dc8ff2afee5.jpg)  
  
    
    - Дальше карусель кредитов - два вращения - активные кредиты / закрытые кредиты.  
![лк9](https://user-images.githubusercontent.com/97405800/182044914-e057958c-f20f-4c08-840a-7afb3b8a2c9c.jpg)  

    - При нажатии на ДЕТАЛИ ссылка уведет на другую страницу (СТАРЫЙ ДИЗАЙН, пока не дошли руки переделать)
    Как видно, здесь сразу графы заполнены данными (номер договора, ежемесячный платёж - их естественно руками можно
    поменять - например, выставить бОльшую сумму)  
        # Cправа таблица платежей - при внесении очередного платежа, там отображается соответствующая информация.
![лк10](https://user-images.githubusercontent.com/97405800/182044980-d6832d60-7e25-409d-8d4c-b0ca7155ed7f.jpg)  

        Соответственно в карусели "погашенные кредиты" такая же информация, а в деталях есть различие :  
![лк11](https://user-images.githubusercontent.com/97405800/182045100-b6a9e767-ab57-4397-bac2-15e2e575e8c9.jpg)  
   
    - Перейдём во "вклады"  
    # сумма вклада автоматически переводится на баланс, если дата закрытия совпадает с текущей датой, 
    либо текущая дата позже, чем дата закрытия. Статус меняется на "Закрыт".
![лк12](https://user-images.githubusercontent.com/97405800/182045133-a46bc96e-f9cd-4fff-84e0-38d15c2e4c28.jpg)  


    - Ну и "инвестиции" :
       # Отображается сумма инвестиций, во что инвестировали, и маржа.
![лк13](https://user-images.githubusercontent.com/97405800/182045234-46dce82e-ffd1-4f18-a3b1-6bf8e96a1b25.jpg)    

##### - Есть готовая страница ИНВЕСТИЦИЙ в самом банке:
![ии1](https://user-images.githubusercontent.com/97405800/182045338-50de82fb-864a-49c3-96b6-9117584b375b.jpg)  
![ии2](https://user-images.githubusercontent.com/97405800/182045365-cb540e89-caf0-40cd-b36e-2605bed715c4.jpg)  
![ии3](https://user-images.githubusercontent.com/97405800/182045441-458c9113-6110-404a-a8ef-109399c24106.jpg)  

    - Инвестировать можно прямо здесь, через модальное окно и POST контроллер Srping MVC  
      # Данные в таблицах автоматически обновляются и пересчитываются.
![ии4](https://user-images.githubusercontent.com/97405800/182045478-8d482acc-e437-4db5-a9fb-3ea3ab9f49e5.jpg)  
  
*** 

##### - Проект в стадии разработки, активно расширяется и улучшается.

##### - <a name="03.08.22">UPD 03.08.22</a>
     - Добавлен сервис восстановления пароля по генерируему секретному коду, высылаемому на email пользователя:  

