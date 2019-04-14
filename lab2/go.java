import ru.ifmo.se.pokemon.*;
public class go
{
	public static void main(String[] args)
	{
		Battle b = new Battle();
		b.addAlly(new Tapu_Fini("", 1));
		b.addAlly(new Rapidash("", 1));
		b.addAlly(new Nidoran_F("", 1));
		b.addFoe(new Nidorina("", 1));
		b.addFoe(new Ponyta("", 1));
		b.addFoe(new Nidoqueen("", 1));
		b.go();
	}
}