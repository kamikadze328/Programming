import ru.ifmo.se.pokemon.*;
public class Tapu_Fini extends Pokemon
{
	public Tapu_Fini(String name, int lvl)
	{
		super(name, lvl);
		setStats(70, 75, 115, 95, 130, 85);
		setType(Type.WATER, Type.FAIRY);
		setMove(new Leafage(), new ZenHeadbutt(), new WorkUp(), new SwordsDance());
	}
}
class Leafage extends PhysicalMove
{
	public Leafage() 
	{
		super(Type.GRASS, 40, 1.0);
	}
}
class ZenHeadbutt extends PhysicalMove
{
	public ZenHeadbutt()
	{
		super(Type.PSYCHIC , 80, 0.9);
	}
	protected void applyOppEffects(Pokemon p)
	{
		if(Math.random() < 0.2)
			Effect.flinch(p);
	}	
}
class WorkUp extends StatusMove
{
	public WorkUp() 
	{
		super(Type.NORMAL, 0, 0.0);
	}
	@Override
	protected boolean checkAccuracy(Pokemon att, Pokemon def)
	{
		return true;
	}
	protected void applySelfEffects(Pokemon p)
	{
		if (p.getStat(Stat.ATTACK) < 6) p.setMod(Stat.ATTACK, +1);
		if (p.getStat(Stat.SPECIAL_ATTACK) < 6) p.setMod(Stat.SPECIAL_ATTACK, +1);
	}
}
class SwordsDance extends StatusMove
{
	public SwordsDance()
	{
		super(Type.NORMAL, 0, 0.0);
	}
	@Override
	protected boolean checkAccuracy(Pokemon att, Pokemon def)
	{
		return true;
	}
	protected void applySelfEffects(Pokemon p)
	{
		if (p.getStat(Stat.ATTACK) < 6)
			p.setMod(Stat.ATTACK, +1);
		if (p.getStat(Stat.ATTACK) < 6)
			p.setMod(Stat.ATTACK, +1);
	}
}