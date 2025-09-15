package projekt.TestKlasse;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import projekt.RaumPlaner;
import projekt.BildUndText.BildUndText;
import projekt.Moebel.Bett;
import projekt.Moebel.Moebel;
import projekt.Moebel.Stuhl;
import projekt.Moebel.Tisch;
import projekt.ObjektListe.ObjektListe;
import projekt.constantsAndCustomExceptions.TooBigException;

class TestKlasseProjekt
{

    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream(); //Dieser Stream ist fuer das Testen vom System.out.println da.
    private final PrintStream originalErr = System.err;
    List<Moebel> list1 = new ArrayList<Moebel>();
    RaumPlaner raumPlaner;
    BildUndText but;
    ObjektListe commandInvoker;

    @BeforeEach
    public void setUp()
    {
        raumPlaner = new RaumPlaner(700, 700, "Keins");
        ObjektListe.ObjektListe.clear();
        but = raumPlaner.getBut();

    }

    @BeforeEach
    public void setUpStream() //Error Stream wird erstellt
    {
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStream() //Ausgangszustand wird wieder hergestellt
    {
        System.setErr(originalErr);
    }

    @Test
    void getHeightDefTest() // Testen des defaults Wert (Height vom Fenster)
    {
        assertEquals(700, raumPlaner.getHeight());
    }

    @Test
    void setHeightTestPos() //Setter Methode Test mit positv Werten
    {
        raumPlaner.setHeight(200);
        assertEquals(200, raumPlaner.getHeight());
    }

    @Test
    void setWidthTestPos() //Setter Methode Test mit positv Werten
    {
        raumPlaner.setWidth(200);
        assertEquals(200, raumPlaner.getWidth());
    }

    @Test
    void setHeightTestNeg() //Setter Methode Test mit negativ Werten
    {
        raumPlaner.setHeight(-200);
        assertEquals("Bitte geben Sie eine gueltige Groesse ein!",
                errContent.toString()
                    .trim()); //trim() wird gemacht, damit beide Strings im richtigen Format sind
    }

    @Test
    void setWidthTestNeg()//Setter Methode Test mit negativ Werten
    {
        raumPlaner.setWidth(-200);
        assertEquals("Bitte geben Sie eine gueltige Groesse ein!",
                errContent.toString()
                    .trim());
    }

    @Test
    void getWidthDefTest() //Testen des defaults Wert (Width vom Fenster)
    {
        assertEquals(700, raumPlaner.getWidth());
    }

    @Test
    void redo() //Hier wird redo getestet
    {
        try
        {
            raumPlaner.addMoebel(new Stuhl(30, 30, Color.BLUE,
                    raumPlaner.getZeichenFlaeche()));
        }
        catch (TooBigException e)
        {
            e.printStackTrace();
        }
        try
        {
            raumPlaner.addMoebel(new Stuhl(40, 40, Color.BLUE,
                    raumPlaner.getZeichenFlaeche()));
        }
        catch (TooBigException e)
        {
            e.printStackTrace();
        }
        list1 = ObjektListe.ObjektListe;
        list1.add(ObjektListe.ObjektListe.get(0));
        raumPlaner.undo();
        assertEquals(list1, ObjektListe.ObjektListe);
        raumPlaner.redo();
        list1.add(ObjektListe.ObjektListe.get(1));
        assertEquals(list1, ObjektListe.ObjektListe);
    }

    @Test
    void undo() //Hier wird undo getestet
    {
        try
        {
            raumPlaner.addMoebel(new Tisch(30, 30, Color.blue,
                    raumPlaner.getZeichenFlaeche()));
        }
        catch (TooBigException e)
        {
            e.printStackTrace();
        }
        try
        {
            raumPlaner.addMoebel(new Tisch(40, 40, Color.blue,
                    raumPlaner.getZeichenFlaeche()));
        }
        catch (TooBigException e)
        {
            e.printStackTrace();
        }

        list1.add(ObjektListe.ObjektListe.get(0));
        list1.add(ObjektListe.ObjektListe.get(1));
        raumPlaner.undo();
        list1.remove(1);
        assertEquals(list1, ObjektListe.ObjektListe);

    }

    //In den folgenden 3 Tests werden die vorhandenen Muster getestet.
    //Uns ist bewusst, dass das neue initialisieren der "raumPlaner" Instanz redundant ist, aber es hat sich nicht gelohnt, Methoden für die Testklasse hinzuzufügen, welche der User garnicht benutzt (BSP: Eine Methode zum Setzen des Musters; dies ist nicht für den Benutzer vorhergesehen).
    //Der Test ist ebenfalls inkonsistent(selten), falls die Größe um bspw. 16 pixel abweicht, dann TestKlasse erneut ausführen
    @Test
    public void testeMusterTeppich()
    {

        raumPlaner = new RaumPlaner(700, 700, "Teppich");
        but = raumPlaner.getBut();

        BufferedImage referenceImg = new BufferedImage(700, 700,
                BufferedImage.TYPE_INT_RGB);
        BufferedImage img = but.getDrawing();

        referenceImg = but.readImage("src/muster/teppich.png");

        try
        {
            Thread.sleep(1000); //Der Timer ist in jeder Test-Methode vorhanden, da es sonst zur Inkonstistenz führen kann. Der Timer sorgt dafür, dass das Bild entsprechend Zeit hat um zu laden(Garantie).
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        }

        assertEquals(img.getWidth(), referenceImg.getWidth(null));
        assertEquals(img.getHeight(), referenceImg.getHeight(null));

        assertTrue(comparePixel(but.getDrawing(), referenceImg));

    }

    @Test
    public void testeMusterLaminat()
    {

        raumPlaner = new RaumPlaner(696, 448, "Laminat");
        but = raumPlaner.getBut();

        BufferedImage referenceImg = new BufferedImage(696, 448,
                BufferedImage.TYPE_INT_RGB);
        BufferedImage img = but.getDrawing();

        referenceImg = but.readImage("src/muster/laminat.png");

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        }

        assertEquals(img.getWidth(), referenceImg.getWidth(null));
        assertEquals(img.getHeight(), referenceImg.getHeight(null));

        assertTrue(comparePixel(but.getDrawing(), referenceImg));

    }

    @Test
    public void testeMusterFliesen()
    {

        raumPlaner = new RaumPlaner(800, 800, "Fliesen");
        but = raumPlaner.getBut();

        BufferedImage referenceImg = new BufferedImage(800, 800,
                BufferedImage.TYPE_INT_RGB);
        BufferedImage img = but.getDrawing();

        referenceImg = but.readImage("src/muster/fliesen.png");

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        }

        assertEquals(img.getWidth(), referenceImg.getWidth(null));
        assertEquals(img.getHeight(), referenceImg.getHeight(null));

        assertTrue(comparePixel(but.getDrawing(), referenceImg));

    }

    // Vergleich 2er Textdateien
    @Test
    void testPoswriteText() throws IOException
    {
        try
        {
            raumPlaner.addMoebel(new Stuhl(30, 30, Color.BLUE,
                    raumPlaner.getZeichenFlaeche()));
            raumPlaner.addMoebel(new Bett(30, 30, Color.BLUE,
                    raumPlaner.getZeichenFlaeche()));
        }
        catch (TooBigException e)
        {
            e.printStackTrace();
        }
        but.writeText("src/testklasse/actualFileWriteTextPos.txt");

        File actualFile = new File("src/testklasse/actualFileWriteTextPos.txt");
        File expectedFile = new File(
                "src/testklasse/expectedFileWriteTest.txt");
        try
        {
            BufferedReader actual = new BufferedReader(
                    new FileReader(actualFile));
            BufferedReader expected = new BufferedReader(
                    new FileReader(expectedFile));
            String expectedLine;
            while ((expectedLine = expected.readLine()) != null)
            {
                String actualLine = actual.readLine();
                assertNotNull(
                        "Das erwartete Dokument hat mehr als das aus das von der Testklasse erstelle Dokument.",
                        actualLine);
                assertEquals(expectedLine, actualLine);
            }

            assertNull(
                    "Das von der Testklasse erstellte Dokument hat mehr Zeilen als das erwartete Dokument.",
                    actual.readLine());
            actual.close();
            expected.close();
        }
        catch (FileNotFoundException e)
        {
            System.out
                .println("Datei nicht gefunden. Bitte Speicherort überprüfen.");
        }

    }

    // Vergleichen 2er Textdateien
    @Test
    void testNegwriteText() throws IOException
    {
        try
        {
            raumPlaner.addMoebel(new Stuhl(30, 30, Color.BLUE,
                    raumPlaner.getZeichenFlaeche()));
            raumPlaner.addMoebel(new Bett(30, 30, Color.BLUE,
                    raumPlaner.getZeichenFlaeche()));
        }
        catch (TooBigException e1)
        {
            e1.printStackTrace();
        }

        but.writeText("src/testklasse/actualFileWriteTextNeg.txt");

        File actualFile = new File("src/testklasse/actualFileWriteTextNeg.txt");
        File expectedFile = new File(
                "src/testklasse/expectedFileWriteTestWrong.txt");
        try
        {
            BufferedReader actual = new BufferedReader(
                    new FileReader(actualFile));
            BufferedReader expected = new BufferedReader(
                    new FileReader(expectedFile));
            String expectedLine;
            while ((expectedLine = expected.readLine()) != null)
            {
                String actualLine = actual.readLine();
                assertNotNull(
                        "Das erwartete Dokument hat mehr als das aus das von der Testklasse erstelle Dokument.",
                        actualLine);
                assertNotEquals(expectedLine, actualLine);
            }

            assertNull(
                    "Das von der Testklasse erstellte Dokument hat mehr Zeilen als das erwartete Dokument.",
                    actual.readLine());
            actual.close();
            expected.close();
        }
        catch (FileNotFoundException e)
        {
            System.out
                .println("Datei nicht gefunden. Bitte Speicherort überprüfen.");
        }

    }

    //ReadText mit einer Datei
    @Test
    void ReadText()
    {
        try
        {
            raumPlaner.addMoebel(new Stuhl(30, 30, Color.BLUE,
                    raumPlaner.getZeichenFlaeche()));
        }
        catch (TooBigException e1)
        {
            e1.printStackTrace();
        }

        try
        {
            raumPlaner.addMoebel(new Tisch(30, 30, Color.BLUE,
                    raumPlaner.getZeichenFlaeche()));
        }
        catch (TooBigException e1)
        {
            e1.printStackTrace();
        }
        list1 = ObjektListe.ObjektListe;
        list1.add(ObjektListe.ObjektListe.get(0));
        list1.add(ObjektListe.ObjektListe.get(1));
        ObjektListe.ObjektListe.clear();
        but.readText("src/testklasse/expectedFileWriteTest.txt");
        assertEquals(list1, ObjektListe.ObjektListe);

    }

    //Hier wird geprüft, ob 2 Möbel, welche die gleiche Positionen beim Spawnen haben miteinander kollidieren oder nicht
    @Test
    void testeKollision()
    {
        try
        {
            raumPlaner.addMoebel(new Stuhl(30, 30, Color.BLUE,
                    raumPlaner.getZeichenFlaeche()));
        }
        catch (TooBigException e1)
        {
            e1.printStackTrace();
        }

        try
        {
            raumPlaner.addMoebel(new Tisch(30, 30, Color.BLUE,
                    raumPlaner.getZeichenFlaeche()));
        }
        catch (TooBigException e1)
        {
            e1.printStackTrace();
        }
        //Die Stühle kriegen ja als X und Y-Wert beide den Wert 0, die Kollision funktioniert wenn deren Position nicht identisch zueinander ist
        double[] Objekt = ObjektListe.ObjektListe.get(0)
            .getGroesseundPos();
        double[] Objekt1 = ObjektListe.ObjektListe.get(1)
            .getGroesseundPos();
        assertEquals(0, Objekt[0]);
        assertEquals(0, Objekt[1]);
        //Das zweite Objekt (Objekt1) müsste die Position 0+laenge, 0+breite haben. Dies wird getestet
        assertEquals(Objekt[2], Objekt1[0]);
        assertEquals(Objekt[3], Objekt1[1]);

    }
    //Hier wird geprüft, ob das Bewegen von Möbelstücken funktioniert (inkl. select)
    @Test
    void TestMove()
    {
    	 try
         {
             raumPlaner.addMoebel(new Stuhl(30, 30, Color.BLUE,
                     raumPlaner.getZeichenFlaeche()));
         }
         catch (TooBigException e1)
         {
             e1.printStackTrace();
         }
        double[] Objekt = ObjektListe.ObjektListe.get(0)
                .getGroesseundPos();
        ObjektListe.ObjektListe.get(0).select(0,0);
        ObjektListe.ObjektListe.get(0).move(30, 30);
        double[] Objekt1 = ObjektListe.ObjektListe.get(0)
                .getGroesseundPos();
        assertEquals(ObjektListe.ObjektListe.get(0).isSelected(),true);
        assertEquals(Objekt[0], 0);
        assertEquals(Objekt[0], 0);
        assertEquals(Objekt1[0], 30);
        assertEquals(Objekt1[1], 30);
    }
 
    //Hier wird geprüft, ob das updaten von Möbeln funktioniert
    @Test
    void TestUpdateMoebel()
    {
    	 try
         {
             raumPlaner.addMoebel(new Stuhl(30, 30, Color.BLUE,
                     raumPlaner.getZeichenFlaeche()));
         }
         catch (TooBigException e1)
         {
             e1.printStackTrace();
         }
    	  double[] Objekt = ObjektListe.ObjektListe.get(0)
                  .getGroesseundPos();
    	  ObjektListe.ObjektListe.get(0).updateMoebel(40,40,Color.BLUE);
    	  double[] Objekt1 = ObjektListe.ObjektListe.get(0)
                  .getGroesseundPos();
    	  assertEquals(Objekt[2],30);
    	  assertEquals(Objekt[3],30);
    	  assertEquals(Objekt1[2],40);
    	  assertEquals(Objekt1[3],40);
    }
    
    //Hilfsmethode zum Vergleichen von Bildern
    boolean comparePixel(Image img, Image referenceImg) //Diese Methode vergleicht die Pixel beider BufferedImages
    {
        boolean result = true;
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        BufferedImage buffImg = (BufferedImage) img;
        BufferedImage referenceBuffImg = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        referenceBuffImg.getGraphics()
            .drawImage(referenceImg, 0, 0, null);

        for (int y = 0; y < height; y++) //Diese for-Schleife geht jedes Pixel durch
        {
            for (int x = 0; x < width; x++)
            {
                if (referenceBuffImg.getRGB(x, y) != buffImg.getRGB(x, y))
                {

                    result = false; //Falls die Bilder nicht identisch sind, soll false ausgegeben werden
                }
            }
        }
        return result; //True, falls die Bilder gleich sind, false wenn nicht
    }
}