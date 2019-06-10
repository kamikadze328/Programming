import java.util.ListResourceBundle;

public class bundle_sl extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {
            {"titleAuth", "Dovoljenje"},
            {"login", "Vpiši se"},
            {"password", "Geslo"},
            {"signUp", "Sign Up"},
            {"logIn", "Vpiši se"},
            {"logTooShort", "Prijava mora biti vsaj 3 znakov"},
            {"passTooShort", "Geslo mora biti vsaj 3 znakov"},
            {"userAlreadyExists", "Uporabnik že obstaja"},
            {"wrongEmail", "Napačen e-poštni naslov"},
            {"userDoesntExist", "Uporabnik ne obstaja"},
            {"waiting", "Čakam ..."},
            {"wrongPassword", "Napačno geslo"},
            {"token", "Žeton:"},
            {"deadToken", "Tvoja roka je konec"},
            {"wrongToken", "Napačna žeton"},
            {"signUpError", "Registrirajte se ni uspela"},
            {"SQLException", "Baza podatkov je na voljo"},
            {"incorrectCommand", " Ukaz je napačna"},
            {"send", "Pošlji"},
            {"title", "Bitje Collection Viewer"},
            {"language", "Jezik"},
            {"exit", "izhod"},
            {"info", "Bitje info"},
            {"name", "Ime"},
            {"family", "Družina"},
            {"hunger", "Lakota"},
            {"location", "Kraj"},
            {"creationTime", "Čas ustvarjanja"},
            {"time", "Čas"},
            {"inventory", "Popis"},
            {"user", "Uporabnik"},
            {"format", "format"},
            {"size", "Velikost"},
            {"change", "Spremeni"},
            {"cancel", "Cancel"},
            {"refresh", "Osveži"},
            {"disconnected", "Strežnik ni na voljo"},
            {"connected", "Povezava: true"},
            {"AddedSuccess", "Dodano Uspeh"},
            {"RemovedSuccess", "Odstranjeno Uspeh"},
            {"SavedSuccess", "Shranjeni Uspeh"},
            {"AddedFailing", "Dodano nasprotnem"},
            {"RemovedFailingDontYours", "Odstranjeno nasprotnem: bitje ne Yours!"},
            {"SavedFailing", "Shranjen ni"},
            {"loadFileError", "Obremenitev v nasprotnem primeru datoteke"},
            {"JSONError", "Neveljavna oblika JSON"},
            {"greeting", "Zanima me, kaj se bo zgodilo ..."},
            {"newCreature", "Nova"},
            {"creatures", "bitja"},
            {"add", "Dodaj"},
            {"add_if_max", "Dodajte če Max"},
            {"remove", "Odstraniti"},
            {"isEmpty", "Polje je prazno!"},
            {"isnNumber", "vrednost ni več!"},
            {"isnPositive", "lahko pozitivno!"},
            {"isnChosen", "ni izbrano"},
            {"added", "Dodano"},
            {"tryAgain", "Strežnik ni na voljo, poskusite znova pozneje"},
            {"incorrectDate", "Datum oblika je"},

            {"ChangedSuccess", "Spremenjeno Uspeh"},
            {"ChangedFailing", "Spremenjeno stanje ni"},
            {"ChangedFailingDontYours", "Spremenjeno Če ne: bitje ne Yours!"},
            {"windowWillBeClosed", "Dead žeton. Okno se bo zaprlo!"},
            {"incorrectX", "Napačna X!"},
            {"incorrectY", "Nepravilno Y!"},
            {"incorrectSize", "Napačna velikost!"},
            {"incorrectHunger", "Napačna Lakota!"},
            {"incorrectDate", "Napačna datum"},
            {"NoCreaturesFound", "Ni bitja"},
            {"deleted", "Črta"},
            {"kek", "Niste povezani<br>LOL"},
            {"color", "Barva"},
            {"clear", "Jasno"}
    };
}
