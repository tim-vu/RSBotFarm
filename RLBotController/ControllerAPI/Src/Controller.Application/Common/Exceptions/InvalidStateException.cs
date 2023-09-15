using System;

namespace Controller.Application.Common.Exceptions;

public class InvalidStateException : ApplicationException
{
    public InvalidStateException(string message) : base(message)
    {
        
    }
}