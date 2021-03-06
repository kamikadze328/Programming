import java.util.ListResourceBundle;

public class bundle_ru extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {
            {"titleAuth", "Авторизация"},
            {"login", "Логин"},
            {"password", "Пароль"},
            {"signUp", "Регистрация"},
            {"logIn", "Вход"},
            {"logTooShort", "Логин должен быть длиннее трёх символов"},
            {"passTooShort", "Пароль должен быть длиннее трёх символов"},
            {"userAlreadyExists", "Такой пользователь уже существует"},
            {"wrongEmail", "Неверный почтовый адрес"},
            {"userDoesntExist", "Пользователь не существует"},
            {"waiting", "Ожидайте..."},
            {"wrongPassword", "Неверный пароль"},
            {"token", "Токен:"},
            {"deadToken", "Ваше время истекло"},
            {"wrongToken", "Неверный токен"},
            {"signUpError", "Не удалось зарегистрироваться"},
            {"SQLException", "База данных недоступна"},
            {"incorrectCommand", " Неверная команда"},
            {"send", "Отправить"},
            {"title", "Существователь"},
            {"language", "Язык"},
            {"exit", "выход"},
            {"info", "Информация о Существе"},
            {"name", "Имя"},
            {"family", "Род"},
            {"hunger", "Голод"},
            {"location", "Местоположение"},
            {"creationTime", "Время создания"},
            {"time", "Время"},
            {"inventory", "Инвентарь"},
            {"user", "Пользователь"},
            {"format", "формат"},
            {"size", "Размер"},
            {"change", "Изменить"},
            {"cancel", "Отмена"},
            {"refresh", "Обновить"},
            {"disconnected", "Сервер недоступен"},
            {"connected", "Соединение: хорошее"},
            {"AddedSuccess", "Успешно добавлено"},
            {"RemovedSuccess", "Успешно удалено"},
            {"SavedSuccess", "Успешно сохранено"},
            {"AddedFailing", "Не удалось добавить"},
            {"RemovedFailingDontYours", "Не удалось удалить: Существо не Ваше Существо"},
            {"SavedFailing", "Не удалось сохранить"},
            {"loadFileError", "Не удалось загрузить файл"},
            {"JSONError", "Файл исписан неразборчивым почерком"},
            {"greeting", "Я предсказывал твоё появление здесь..."},
            {"newCreature", "Новое"},
            {"creatures", "существо"},
            {"add", "Добавить"},
            {"add_if_max", "Добавить(Max)"},
            {"remove", "Удалить"},
            {"isEmpty", "не заполнено!"},
            {"isnNumber", "не число!"},
            {"isnPositive", "должен быть больше нуля!"},
            {"isnChosen", "не выбрано"},
            {"added", "Добавлено"},
            {"tryAgain", "Сервер недоступен, попробуйте ещё раз"},
            {"incorrectDate", "Формат даты: "},
            {"ChangedSuccess", "Успешно изменено"},
            {"ChangedFailing", "Не удалось изменить"},
            {"ChangedFailingDontYours","Не удалось изменить: не Ваше Существо!"},
            {"windowWillBeClosed", "Ваше время истекло. Окно будет закрыто!"},
            {"incorrectX", "Некорректный X!"},
            {"incorrectY", "Некорректный Y!"},
            {"incorrectSize", "Некорректный Размер!"},
            {"incorrectHunger", "Некорректный уровень Голода!"},
            {"incorrectDate", "Некорректная Дата"},
            {"NoCreaturesFound", "Существ не найдено"},
            {"deleted", "Удалено"},
            {"kek", "Вы никуда не подключены<br>ЛОЛ"},
            {"color", "Цвет"},
            {"clear", "Очистить"},
            {"CreaturesDoesntChanged", "Существа не изменились"},
            {"ChangedFailedCreatureExists", "Не удалось изменить: Существо уже есть!"},
            {"AddedFailedCreatureExists", "Не удалось добавить: Существо уже есть!"}
    };
}
