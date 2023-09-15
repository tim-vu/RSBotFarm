using System;
using System.Linq;
using Namotion.Reflection;
using NJsonSchema.Annotations;
using NJsonSchema.Generation;

namespace Controller.Application.Infrastructure.NSwag;

public class SchemaNameGenerator : DefaultSchemaNameGenerator
{
    public override string Generate(Type type)
    {
        var cachedType = type.ToCachedType();

        var jsonSchemaAttribute = cachedType.GetInheritedAttribute<JsonSchemaAttribute>();
        if (!string.IsNullOrEmpty(jsonSchemaAttribute?.Name))
        {
            return jsonSchemaAttribute.Name;
        }

        var nType = type.ToCachedType();
        
        if (nType.Type.IsConstructedGenericType)
        {
            return GetName(nType).Split('`').First() + "Of" +
                   string.Join("And", nType.GenericArguments
                       .Select(a => Generate(a.OriginalType)));
        }
        
        if (type.IsNested && (type.Name.Equals("Command") || type.Name.Equals("Query")))
            return type.DeclaringType!.Name;
        
        const string dtoSuffix = "Dto";
        
        if (type.Name.EndsWith(dtoSuffix))
            return type.Name.Substring(0, type.Name.Length - dtoSuffix.Length);

        return GetName(nType);
    }
    
    private static string GetName(CachedType cType)
    {
        return
            cType.TypeName == "Int16" ? GetNullableDisplayName(cType, "Short") :
            cType.TypeName == "Int32" ? GetNullableDisplayName(cType, "Integer") :
            cType.TypeName == "Int64" ? GetNullableDisplayName(cType, "Long") :
            GetNullableDisplayName(cType, cType.TypeName);
    }

    private static string GetNullableDisplayName(CachedType type, string actual)
    {
        return (type.IsNullableType ? "Nullable" : "") + actual;
    }
}