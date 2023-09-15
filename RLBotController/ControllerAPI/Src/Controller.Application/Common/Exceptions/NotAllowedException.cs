using System;

namespace Controller.Application.Common.Exceptions;

public class NotAllowedException : ApplicationException
{
    public NotAllowedException(string message) : base(message)
    {
        
    }
}