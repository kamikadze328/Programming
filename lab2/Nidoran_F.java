import ru.ifmo.se.pokemon.*;
public class Nidoran_F extends Pokemon
{
	public Nidoran_F(String name, int lvl)
	{
		super(name, lvl);
		setStats(55, 47, 52, 40, 40, 41);
		setType(Type.POISON);
		setMove(new Blizzard(), new DoubleTeam());		
	}
}
class Blizzard extends SpecialMove
{
	public Blizzard()
	{
		super(Type.ICE, 110, 0.7);
	}
	@Override
	protected void applyOppEffects(Pokemon p)
	{
		if (Math.random() < 0.1) 
			Effect.freeze(p);
	}
}
class DoubleTeam extends StatusMove
{
	public DoubleTeam()
	{
		super(Type.NORMAL, 0, 0.0);
	}
	@Override
	protected boolean checkAccuracy(Pokemon att, Pokemon def)
	{
		return true;
	}
	@Override
	protected void applySelfEffects(Pokemon p)
	{
		if (p.getStat(Stat.EVASION) < 6) p.setMod(Stat.EVASION, +1);
	}
}