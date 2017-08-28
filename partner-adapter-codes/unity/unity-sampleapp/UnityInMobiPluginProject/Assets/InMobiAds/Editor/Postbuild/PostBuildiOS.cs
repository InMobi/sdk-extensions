namespace InMobiAds.Editor.Postbuild
{
	using UnityEngine;
	using System.Collections;
	using System.Collections.Generic;
	using System.IO;
	using InMobiAds.Editor.ThirdParty.xcodeapi;

	public class PostBuildiOS
	{
		protected static void AddPlatformFrameworksToProject(string[] frameworks, PBXProject project, string target)
		{
			foreach (string framework in frameworks) {
				if (!project.HasFramework (framework)) {
					Debug.Log ("Adding " + framework + " to Xcode project");
					project.AddFrameworkToProject (target, framework, false);
				}
			}
		}
		
		protected static void AddPlatformLibsToProject(string[] libs, PBXProject project, string target)
		{
			foreach (string lib in libs) {
				string libGUID = project.AddFile ("usr/lib/" + lib, "Libraries/" + lib, PBXSourceTree.Sdk);
				project.AddFileToBuild (target, libGUID);
			}	
		}

		protected static void AddBuildProperty(PBXProject project, string target, string property, string value)
		{
			project.AddBuildProperty (target, property, value);
		}
	}

}
