import ru.ifmo.se.pokemon.*;
public class Ponyta extends Pokemon
{
	public Ponyta(String name, int lvl)
	{
		super(name, lvl);
		setStats(50, 85, 55, 65, 65, 90);
		setType(Type.FIRE);
		setMove(new TakeDown(), new Will_O_Wisp(), new FireBlast());	
	}
}
class TakeDown extends PhysicalMove
{
	public TakeDown()
	{
		super(Type.NORMAL, 90, 0.85);
	}
	@Override
	protected void applySelfDamage(Pokemon att, double damage)
	{
		att.setMod(Stat.HP, (int) Math.round(damage/4));
	}
}
class Will_O_Wisp extends StatusMove
{
	public Will_O_Wisp()
	{
		super(Type.FIRE, 0, 0.85);
	}
	@Override
	protected void applyOppEffects(Pokemon p)
	{
		Effect.burn(p);
	}
}
class FireBlast extends SpecialMove
{
	public FireBlast()
	{
		super(Type.FIRE, 110, 0.85);
	}
	@Override
	protected void applyOppEffects(Pokemon p)
	{
		if (Math.random() < 0.1) 
			Effect.burn(p);
	}
}