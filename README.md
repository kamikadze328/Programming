# Programming
Несколько лабораторных работ по программированию на кафедре ВТ(ИТМО)
+ Отдельно 1, 2, 6 и 7 работы
+ При копипасте шестой лучше подглядывайте параллельно в седьмую)
+ Третья и четвёртые работы включены в пятую. Однако, можно найти исходный код в отчётах(docx, pdf)
+ Две полезных книжки (Для защиты и выполнения лабораторных до пятой включительно)
+ Задания:
+ lab6:
Разделить программу из лабораторной работы №5 на клиентский и серверный модули. Серверный модуль должен осуществлять выполнение команд по управлению коллекцией. Клиентский модуль должен в интерактивном режиме считывать команды, передавать их для выполнения на сервер и выводить результаты выполнения. Команда import должна использовать файл из файловой системы клиента (содержимое файла передается на сервер), load и save - сервера.
Хранящиеся в коллекции объекты должны иметь следующие характеристики:
  1.	имя, название или аналогичный текстовый идентификатор;
  2.	размер или аналогичный числовой параметр;
  3.	характеристику, определяющую местоположение объекта на плоскости/в пространстве;
  4.	время/дату рождения/создания объекта.
  5.	Если аналогичные характеристики уже есть, добавлять их не нужно.
  6.	Необходимо выполнить следующие требования:
  7.	Коллекцию из ЛР №5 заменить на ее потокобезопасный аналог.
  8.	Операции обработки объектов коллекции должны быть реализованы с помощью Stream API с использованием лямбда-выражений.
  9.	Объекты между клиентом и сервером должны передаваться в сериализованном виде.
  10.	Объекты в коллекции, передаваемой клиенту, должны быть отсортированы по названию.
  11.	Получив запрос, сервер должен создавать отдельный поток, который должен формировать и отправлять ответ клиенту.
  12.	Клиент должен корректно обрабатывать временную недоступность сервера.
  13.	Обмен данными между клиентом и сервером должен осуществляться по протоколу TCP.
  14.	На стороне сервера должен использоваться потоки ввода-вывода а на стороне клиента - сетевой канал.

+ lab7:
  Доработать программу из лабораторной работы №6 следующим образом:
  1.	В класс, объекты которого хранятся в коллекции, добавить поле типа java.time.OffsetDateTime, в котором должны храниться дата и время создания объекта.
  2.	Обеспечить возможность регистрации и авторизации пользователей.
  3.	Пользователь задаёт пароль при регистрации.
  4.	При регистрации отправлять на почту пользователя случайный токен для подтверждения регистрации. Срок жизни токена - 1,5 мин.
  5.	Для отправки почтовых уведомлений использовать JavaMail API.
  6.	Пароли при хранении хэшировать алгоритмом SCrypt.
  7.	Реализовать ассоциацию между объектом из коллекции и пользователем, его создавшим. Пользователи могут просматривать объекты созданные всеми пользователями, а модифицировать - только свои.
	8.	Для идентификации запроса пользователя использовать случайно сгенерированный токен.
	9.	Инвалидировать токен через 1,5 мин. после последнего запроса.
	10.	Оповещать всех пользователей о подключении и отключении других пользователей(в том числе и об отключении по таймауту).
	11.	Обеспечить хранение всех данных (объектов коллекции, пользователей и ассоциаций) в реляционной базе данных
	
+ lab8:
	Доработать программу из лабораторной работы №7 следующим образом: 
	Заменить консольный клиент на клиент с графическим интерфейсом пользователя(GUI). В функционал клиента должно входить:  
	1. Окно с авторизацией/регистрацией. 
	2. Отображение текущего пользователя. 
	3. Таблица, отображающая все объекты из коллекции 
	4. Каждое поле объекта - отдельная колонка таблицы. 
	5. Строки таблицы можно фильтровать/сортировать по значениям любой из колонок. Сортировку и фильтрацию значений столбцов реализовать с помощью Streams API. 
	6. Поддержка всех команд из предыдущих лабораторных работ. 
	7. Область, визуализирующую объекты коллекции 
	8. Объекты должны быть нарисованы с помощью графических примитивов с использованием Graphics, Canvas или аналогичных средств графической библиотеки. 
	9. При визуализации использовать данные о координатах и размерах объекта. 
	10. Объекты от разных пользователей должны быть нарисованы разными цветами. 
	11. При нажатии на объект должна выводиться информация об этом объекте. 
	12. При добавлении/удалении/изменении объекта, он должен автоматически появиться/исчезнуть/измениться на области как владельца, так и всех других клиентов. 
	13. При отрисовке объекта должна воспроизводиться согласованная с преподавателем анимация. 
	14. Возможность редактирования отдельных полей любого из объектов (принадлежащего пользователю). Переход к редактированию объекта возможен из таблицы с общим списком объектов и из области с визуализацией объекта. 
	15. Возможность удаления выбранного объекта (даже если команды remove ранее не было). 
	16. Интерфейс должен быть реализован с помощью библиотеки Swing 
	17. Графический интерфейс клиентской части должен поддерживать русский, словенский, шведский и английский (Австралия) языки / локали. Должно обеспечиваться корректное отображение чисел, даты и времени в соответстии с локалью. Переключение языков должно происходить без перезапуска приложения. Локализованные ресурсы должны храниться в классе.
![](example8.gif)
