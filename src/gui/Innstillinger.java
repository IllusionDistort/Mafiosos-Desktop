package gui;

import Utils.TvUtil;
import Utils.VeiledningsUtil;
import datastruktur.Spillerliste;
import personer.Spiller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class Innstillinger extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    Oppstart oppstart;
    Spill spill;
    Spillerliste spillere;
    Vindu vindu;
    JMenuBar menuBar;
    JPanel innhold;

    public Innstillinger(String tittel, Oppstart o, Spillerliste sl) {
        super(tittel);
        oppstart = o;
        vindu = oppstart.vindu;
        spillere = sl;

        start();
    }

    public Innstillinger(String tittel, Spill s, Spillerliste sl) {
        super(tittel);
        spill = s;
        vindu = spill.vindu;
        spillere = sl;

        start();
    }

    private void start() {
        setVisible(true);
        setMinimumSize(new Dimension(400, 400));
        setLocationRelativeTo(null);

        innhold = new JPanel();
        add(innhold);

        knapper();
    }

    public void setSpill(Spill s) {
        spill = s;
    }

    public void knapper() {
        Knapp veiledning = new Knapp("Vis/skjul veiledning", Knapp.HEL, this);
        innhold.add(veiledning);
        Knapp fullskjerm = new Knapp("Fullskjerm TV", Knapp.HEL, this);
        innhold.add(fullskjerm);
        Knapp dagtid = new Knapp("Sett dagtid", Knapp.HEL, this);
        innhold.add(dagtid);
        Knapp font = new Knapp("Sett skriftstørrelse", Knapp.HEL, this);
        innhold.add(font);
        Knapp forteller = new Knapp("Fortellerinfo", Knapp.HEL, this);
        innhold.add(forteller);
        if (oppstart != null && oppstart.fase == Oppstart.VELGROLLER) {
            Knapp leggTilSpiller = new Knapp("Legg til spiller", Knapp.HEL,
                    this);
            innhold.add(leggTilSpiller);
        }
        if (spill != null) {
            if (spill.dag) {
                if (spill.vindu.kontroll.isVisible()) {
                    Knapp røm = new Knapp("Røm!", Knapp.HEL, this);
                    innhold.add(røm);
                }
                if (spill.aktivTimer()) {
                    Knapp pause = new Knapp("Start/stopp klokka", Knapp.HEL,
                            this);
                    innhold.add(pause);
                }
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

        if (((Knapp) e.getSource()).getText() == "Vis/skjul veiledning") {
            VeiledningsUtil.visSkjulVeiledning();
        }

        if (((Knapp) e.getSource()).getText() == "Sett dagtid") {
            String input = JOptionPane
                    .showInputDialog("Hvor lang skal dagen være? (Minutter)");
            if (input.matches("\\d{1,2}")) {
                int min = Integer.parseInt(input);
                if (spill == null)
                    oppstart.setTid(min);
                else
                    spill.setTid(min);
            }
        }

        if (((Knapp) e.getSource()).getText() == "Sett skriftstørrelse") {
            String input = JOptionPane
                    .showInputDialog("Skriv inn skriftstørrelse (Standard: 30)");
            if (input.matches("\\d{1,2}")) {
                int størrelse = Integer.parseInt(input);
                TvUtil.setFont(størrelse);
            }
        }

        if (((Knapp) e.getSource()).getText() == "Fortellerinfo") {
            String input = JOptionPane
                    .showInputDialog("Fortellers telefonnummer:");
            if (input.matches("\\d{8}"))
                visFortellerInfo(Integer.parseInt(input));
            else
                JOptionPane.showMessageDialog(this,
                        "Telefonnummer må inneholde 8 siffer!");
        }

        if (((Knapp) e.getSource()).getText() == "Fullskjerm TV") {
            TvUtil.visSkjulRamme();
        }

        if (((Knapp) e.getSource()).getText() == "Legg til spiller") {
            String input = JOptionPane.showInputDialog("Navn på spiller:");
            if (input.length() == 0)
                return;
            if (spillere.finnSpillerMedNavn(input) != null)
                JOptionPane.showMessageDialog(this, "Finnes allerede!");
            else
                leggTilSpiller(input);
        }

        if (((Knapp) e.getSource()).getText() == "Røm!") {
            spill.rømning();
        }

        if (((Knapp) e.getSource()).getText() == "Start/stopp klokka") {
            spill.timer.playPause();
        }
    }

    // KNAPPEFUNKSJONER
    public void leggTilSpiller(String navn) {
        if (spillere.finnSpillerMedNavn(navn) == null) {
            spillere.leggTil(new Spiller(navn));
            oppstart.antallspillere++;
            oppstart.informer(spillere.rolleString(oppstart.roller,
                    oppstart.antallspillere));
        }
    }

    public void visFortellerInfo(int tlf) {
        JFrame fortellervindu = new JFrame("Forteller:");
        fortellervindu.setVisible(true);
        fortellervindu.setMinimumSize(new Dimension(300, 80));
        fortellervindu.setResizable(false);
        JPanel tlfInfo = new JPanel();
        JLabel tlfLab = new JLabel("Tlf: " + tlf);
        tlfLab.setAlignmentX(CENTER_ALIGNMENT);
        tlfLab.setAlignmentY(CENTER_ALIGNMENT);
        tlfLab.setFont(new Font("Sans", Font.BOLD, 30));
        tlfInfo.add(tlfLab);
        fortellervindu.add(tlfInfo);
    }
}
