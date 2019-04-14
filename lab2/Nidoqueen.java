import ru.ifmo.se.pokemon.*;
public class Nidoqueen extends Nidorina
{
	public Nidoqueen(String name, int lvl)
	{
		super(name, lvl);
		setStats(90, 92, 87, 75, 85, 76);
		addMove(new DoubleTeam());
	}
}