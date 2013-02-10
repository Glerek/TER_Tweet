/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Analyse;

import java.io.IOException;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import org.annolab.tt4j.*;

/**
 *
 * @author Glerek
 */
public class TT4J {
    private String _pathTreeTagger = "src\\resources\\tree-tagger.exe";
    private ArrayList<String> positionTt4j;
    private ArrayList<String> lemmaTt4j;

    /**
     * @param args the command line arguments
     */
    public TT4J(String s) throws IOException, TreeTaggerException {
        positionTt4j = new ArrayList<String>();
        lemmaTt4j = new ArrayList<String>();
        System.setProperty("treetagger.home", "src\\resources\\tree-tagger.exe");
        TreeTaggerWrapper tt = new TreeTaggerWrapper<String>();
        try {
            tt.setModel("src\\resources\\french.par");
            tt.setExecutableProvider(new ExecutableResolver()     {				
	@Override
	public void setPlatformDetector(PlatformDetector arg0) {}
 
	@Override
	public String getExecutable() throws IOException 
	{
		return _pathTreeTagger;
	}
	@Override
	public void destroy() {}				
});
            tt.setHandler(new TokenHandler<String>() {
                public void token(String token, String pos, String lemma) {
                    if(pos == "NOM" || pos.indexOf("VER") != -1) {
                        //System.out.println(token + "\t" + pos + "\t" + lemma);
                        positionTt4j.add(pos);
                        lemma = lemma.replaceAll("[\\W]","");
                        lemmaTt4j.add(lemma);
                    }
                }
            });
            String[] str = s.split(" ");
            tt.process(asList(str));
        }
        finally {
                tt.destroy();
        }
    }
    
    public ArrayList<String> getPos() {
        return positionTt4j;
    }
    
    public ArrayList<String> getLemma() {
        return lemmaTt4j;
    }
}
