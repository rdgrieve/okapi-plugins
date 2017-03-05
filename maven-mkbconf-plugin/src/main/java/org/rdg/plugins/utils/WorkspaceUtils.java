package org.rdg.plugins.utils;

import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Class containing utility methods for creating a temporary workspace.
 * 
 */
public final class WorkspaceUtils
{
    
    private WorkspaceUtils() {
        //prevent instantiation
    }


    private static final String LOCAL_WORKSPACE = "workspace";

    /**
     * Creates a new folder to be used as a workspace.
     * Note - this only creates the folder - it doesn't turn it into an eclipse workspace.
     * @param workspaceParent The parent directory under which to create the workspace.
     * @return A File for the newly created directory.
     * @throws MojoExecutionException Is an error occurs.
     */
    public static File createWorkspaceFolder(File workspaceParent) throws MojoExecutionException
    {
        String workspaceString = workspaceParent.getAbsolutePath() + File.separator
        + LOCAL_WORKSPACE;
        File workspace = new File(workspaceString);
        if (workspace.exists()) {
            //delete silently
            workspace.delete();
        }
        if (!workspace.mkdirs()) {
            throw new MojoExecutionException("Unable to create unique workspace "
                    + workspace.getAbsolutePath());
        }
        return workspace;
    }
    
    /**
     * Destroys a folder/eclipse workspace by deleting the folder and all it's contents.
     * @param workspace The workspace to delete.
     * @throws MojoExecutionException If an error occurs.
     */
    public static void destroyWorkspace(File workspace) throws MojoExecutionException
    {
        try {
            FileUtils.deleteDirectory(workspace);
        } catch (IOException e) {
            throw new MojoExecutionException("Unable to delete temp workspace "
                    + workspace.getAbsolutePath(), e);
        }
    }
}
