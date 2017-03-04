package org.rdg.plugins;

/**
 * Represents an individual filter mapping from an extension to a specific filter configuration.
 */
public class FilterMapping
{

    private String extension;

    private String filterConfig;


    /**
     * Get the file extension.
     * @return The file extension.
     */
    public String getExtension()
    {
        return extension;
    }

    /**
     * Set the file extension.
     * @param extension The file extension.
     */
    public void setExtension(String extension)
    {
        this.extension = extension;
    }

    /**
     * Get the filter config.
     * @return filter config.
     */
    public String getFilterConfig()
    {
        return filterConfig;
    }

    /**
     * Set the filter config.
     * @param filterConfig the filter config.
     */
    public void setFilterConfig(String filterConfig)
    {
        this.filterConfig = filterConfig;
    }


    public String toString() {
        return "FilterMapping('" + extension + "' --> " + filterConfig + ")";
    }

}
