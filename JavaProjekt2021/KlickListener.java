package projekt.Listener;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import projekt.GUI.RaumPlanerMoebelstueckAendernGUI;
import projekt.Moebel.Moebel;
import projekt.ObjektListe.ObjektListe;
import projekt.Zeichenflaeche.ZeichenFlaeche;

/**
 * Hier werden die Mausklicks vom User verarbeitet
 */
public class KlickListener extends MouseAdapter
{
    private Point mousePt;
    private ZeichenFlaeche zeichenflaeche;
    private ObjektListe objektListe;

    public KlickListener(ZeichenFlaeche ZeichenFlaeche)
    {
        zeichenflaeche = ZeichenFlaeche;
        objektListe = new ObjektListe();
    }

    /**
     * Hier wird die entsprechende registrierte Maustaste vom User entsprechend verarbeitet (Select,Rotation, Änderung, Löschung)
     */
    public void mousePressed(MouseEvent e)
    {
        mousePt = e.getPoint(); //aktueller Punkt wird registriert
        for (Moebel d : ObjektListe.ObjektListe)
        {
            d.select(mousePt.x, mousePt.y); //Falls ein Objekt in dem registriert Punkt vorhanden ist, wird dieses selected
            if (d.isSelected())
            {
                d.checkKollision();//Kollision wird geprüft
                if (SwingUtilities.isRightMouseButton(e)) //Falls Rechte Maustaste geklickt wird
                {
                    d.rotieren(); //Rotierung
                    zeichenflaeche.repaint();
                    d.unselect();// Nach der Rotierung ist das Objekt nicht mehr selektiert
                }
                else if (SwingUtilities.isMiddleMouseButton(e)) //Mittlere Maustaste
                {
                    objektListe.removeCommand(d); //Das Objekt wird gelöscht
                    zeichenflaeche.repaint();
                }
                else if (e.getClickCount() == 2 //Bei zweifachen Linksklick kann man das Objekt anpassne
                        && e.getButton() == MouseEvent.BUTTON1)
                {
                    new RaumPlanerMoebelstueckAendernGUI(zeichenflaeche,
                            objektListe, d);
                }
                break;
            }
        }
    }

    /**
     * Falls die Maustaste losgelassen wird, ist das Objekt auch nicht mehr selektiert
     */
    public void mouseReleased(MouseEvent e)
    {
        for (Moebel d : ObjektListe.ObjektListe)
            d.unselect(); // button up--nobody should move any more
    }
}