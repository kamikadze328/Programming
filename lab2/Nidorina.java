import ru.ifmo.se.pokemon.*;
public class Nidorina extends Nidoran_F
{
	public Nidorina(String name, int lvl)
	{
		super(name, lvl);
		setStats(70, 62, 67, 55, 55, 56);
		addMove(new Flatter());
	}
}
class Flatter extends StatusMove
{
	public Flatter()
	{
		super(Type.DARK, 0, 1.0);
	}
	protected void applyOppEffects(Pokemon p)
	{
		Effect.confuse(p);
		if (p.getStat(Stat.SPECIAL_ATTACK) < 6)
			p.setMod(Stat.SPECIAL_ATTACK, +1);
		if (p.getStat(Stat.SPECIAL_ATTACK) < 6)
			p.setMod(Stat.SPECIAL_ATTACK, +1);
	}
}