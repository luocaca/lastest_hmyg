package com.white.update;

public class UpdateInfo
{
        private String version;
        private String description;
        private String url;
        private boolean isForce;
        
        public boolean isForce() {
			return isForce;
		}
		public void setForce(boolean isForce) {
			this.isForce = isForce;
		}
		public String getVersion()
        {
                return version;
        }
        public void setVersion(String version)
        {
                this.version = version;
        }
        public String getDescription()
        {
                return description;
        }
        public void setDescription(String description)
        {
                this.description = description;
        }
        public String getUrl()
        {
                return url;
        }
        public void setUrl(String url)
        {
                this.url = url;
        }
        
}
